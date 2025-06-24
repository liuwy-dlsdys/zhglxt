package com.zhglxt.common.ipLocation.maker;

import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Maker {
    // constants define
    public static final int VersionNo = 2;
    public static final int HeaderInfoLength = 256;
    public static final int VectorIndexRows = 256;
    public static final int VectorIndexCols = 256;
    public static final int VectorIndexSize = 8;
    public static final int SegmentIndexSize = 14;
    public static final int VectorIndexLength = VectorIndexRows * VectorIndexCols * VectorIndexSize;

    public static final Log log = Log.getLogger(Maker.class);

    // source text file handle
    private final File srcFile;
    private final int[] fields;
    private final List<Segment> segments;
    private final Charset bytesCharset;

    // destination binary file handle
    private final RandomAccessFile dstHandle;

    // index policy
    private final int indexPolicy;

    // region pool
    private final Map<String, DataEntry> regionPool;

    // vector index raw bytes
    private final byte[] vectorIndex;

    public Maker(int policy, String srcPath, String dstPath, int[] fields) throws IOException {
        this.srcFile = new File(srcPath);
        if (!this.srcFile.exists()) {
            throw new FileNotFoundException("source text file `" + srcPath + "` not found");
        }

        this.fields = fields;

        /// check and delete the target xdb file if it exists
        /// final File dstFile = new File(dstPath);
        /// if (dstFile.exists() && !dstFile.delete()) {
        ///     log.warnf("failed to delete the dest xdb file `%s`", dstPath);
        /// }

        this.bytesCharset = Charset.forName("utf-8");
        this.segments = new LinkedList<Segment>();
        this.dstHandle = new RandomAccessFile(dstPath, "rw");
        this.indexPolicy = policy;
        this.regionPool = new HashMap<String, DataEntry>();
        this.vectorIndex = new byte[VectorIndexLength]; // all filled with 0

        // truncate the original xdb file
        this.dstHandle.setLength(0);
    }

    // init the header of the target xdb binary file
    private void initHeader() throws IOException {
        log.infof("try to init the db header ... ");
        dstHandle.seek(0);

        // make and write the header space
        final byte[] header = new byte[HeaderInfoLength];

        // encode the data
        Util.write(header, 0, VersionNo, 2);
        Util.write(header, 2, indexPolicy, 2);
        Util.write(header, 4, System.currentTimeMillis() / 1000, 4);
        Util.write(header, 8, 0, 4);    // start index ptr
        Util.write(header, 12, 0, 4);   // end index ptr

        dstHandle.write(header);
    }

    // internal method to apply the region fields filter
    private String getFilteredRegion(String region) {
        if (this.fields.length == 0) {
            return region;
        }

        final String[] fs = region.split("\\|", -1);
        final StringBuilder sb = new StringBuilder();
        final int tailing = this.fields.length - 1;
        for (int i = 0; i < this.fields.length; i++) {
            final int idx = this.fields[i];
            if (idx >= fs.length) {
                throw new IllegalArgumentException("field index `"
                        + idx + "` exceeded the max length `" + fs.length + "`");
            }

            sb.append(fs[idx]);
            if (sb.length() > 0 && i < tailing) {
                sb.append("|");
            }
        }

        return sb.toString();
    }

    // load all the segments
    private void loadSegments() throws Exception {
        log.infof("try to load the segments ... ");
        final long tStart = System.currentTimeMillis();
        Segment last = null;
        String line;

        final FileInputStream fis = new FileInputStream(srcFile);
        final BufferedReader br = new BufferedReader(new InputStreamReader(fis, bytesCharset));
        while ((line = br.readLine()) != null) {
            log.debugf("load segment `%s`", line);
            final String[] ps = line.split("\\|", 3);
            if (ps.length != 3) {
                br.close();
                throw new Exception("invalid ip segment line `"+ps[0]+"`");
            }

            final long sip = Util.checkIP(ps[0]);
            final long eip = Util.checkIP(ps[1]);
            if (sip > eip) {
                br.close();
                throw new Exception("start ip("+ps[0]+") should not be greater than end ip("+ps[1]+")");
            }

            if (ps[2].isEmpty()) {
                br.close();
                throw new Exception("empty region info in segment line `"+ps[2]+"`");
            }

            // check the continuity of the data segment
            if (last != null) {
                if (last.endIP + 1 != sip) {
                    br.close();
                    throw new Exception("discontinuous data segment: last.eip+1("+sip+") != seg.sip("+eip+", "+ps[0]+")");
                }
            }

            final Segment seg = new Segment(sip, eip, getFilteredRegion(ps[2]));
            segments.add(seg);
            last = seg;
        }

        br.close();
        log.infof("all segments loaded, length: %d, elapsed: %d ms", segments.size(), System.currentTimeMillis() - tStart);
    }

    // init the maker
    public void init() throws Exception {
        // init the db header
        initHeader();

        // load all the segments
        loadSegments();
    }

    // set the vector index info of the specified ip
    private void setVectorIndex(long ip, long ptr) {
        final int il0 = (int) ((ip >> 24) & 0xFF);
        final int il1 = (int) ((ip >> 16) & 0xFF);
        final int idx = il0 * VectorIndexCols * VectorIndexSize + il1 * VectorIndexSize;
        final long sPtr = Util.getIntLong(vectorIndex, idx);
        if (sPtr == 0) {
            Util.write(vectorIndex, idx, ptr, 4);
            Util.write(vectorIndex, idx + 4, ptr + SegmentIndexSize, 4);
        } else {
            Util.write(vectorIndex, idx + 4, ptr + SegmentIndexSize, 4);
        }
    }

    // start to make the binary file
    public void start() throws Exception {
        if (segments.isEmpty()) {
            throw new Exception("empty segment list");
        }

        // 1, write all the region/data to the binary file
        dstHandle.seek(HeaderInfoLength + VectorIndexLength);

        log.infof("try to write the data block ... ");
        for (Segment seg : segments) {
            log.debugf("try to write region `%s` ... ", seg.region);
            final DataEntry e = regionPool.get(seg.region);
            if (e != null) {
                log.debugf(" --[Cached] with ptr=%d", e.ptr);
                continue;
            }

            // get the utf-8 bytes of the region info
            final byte[] regionBuff = seg.region.getBytes(bytesCharset);
            if (regionBuff.length < 1) {
                throw new Exception("empty region info for segment `"+seg+"`");
            } else if (regionBuff.length > 0xFFFF) {
                throw new Exception("too long region info `"+seg.region+"`: should be less than 65535 bytes");
            }

            // record the current ptr
            final long pos = dstHandle.getFilePointer();
            dstHandle.write(regionBuff);

            // record the mapping
            regionPool.put(seg.region, new DataEntry(regionBuff.length, pos));
            log.debugf(" --[Added] with ptr=%d", pos);
        }

        // 2, write the index block cache the super index block
        log.infof("try to write the segment index block ... ");
        int counter = 0;
        long startIndexPtr = -1, endIndexPtr = -1;
        final byte[] indexBuff = new byte[SegmentIndexSize];    // 4 + 4 + 2 + 4
        for (Segment seg : segments) {
            // we need the region ptr
            DataEntry e = regionPool.get(seg.region);
            if (e == null) {
                throw new Exception("missing ptr cache for region `"+seg.region+"`");
            }

            List<Segment> segList = seg.split();
            log.debugf("try to index segment(%d splits) %s ... ", segList.size(), seg);
            for (Segment s : segList) {
                long pos = dstHandle.getFilePointer();

                // encode the segment index info
                Util.write(indexBuff,  0, s.startIP, 4);
                Util.write(indexBuff,  4, s.endIP, 4);
                Util.write(indexBuff,  8, e.length, 2);
                Util.write(indexBuff, 10, e.ptr, 4);
                dstHandle.write(indexBuff);

                log.debugf("|-segment index: %d, ptr: %d, segment: %s", counter, pos, s);
                setVectorIndex(s.startIP, pos);
                counter++;

                // check and record the start index ptr
                if (startIndexPtr == -1) {
                    startIndexPtr = pos;
                }

                endIndexPtr = pos;
            }
        }

        // 3, synchronize the vector index block
        log.infof("try to write the vector index block ... ");
        dstHandle.seek(HeaderInfoLength);
        dstHandle.write(vectorIndex);

        // 4, synchronize the segment index info
        log.infof("try to write the segment index ptr ... ");
        Util.write(indexBuff, 0, startIndexPtr, 4);
        Util.write(indexBuff, 4, endIndexPtr, 4);
        dstHandle.seek(8);
        dstHandle.write(indexBuff, 0, 8);

        log.infof("write done, dataBlocks: %d, indexBlocks: (%d, %d), indexPtr: (%d, %d)",
            regionPool.size(), segments.size(), counter, startIndexPtr, endIndexPtr);
    }

    // end the make, do the resource clean up
    public void end() throws IOException {
        this.dstHandle.close();
    }

    private static class DataEntry {
        final long ptr;
        final int length; // in bytes

        DataEntry(int length, long ptr) {
            this.length = length;
            this.ptr = ptr;
        }
    }

}
