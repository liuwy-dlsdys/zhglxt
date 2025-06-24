package com.zhglxt.common.ipLocation;


public class Header {
    public final int version;
    public final int indexPolicy;
    public final int createdAt;
    public final int startIndexPtr;
    public final int endIndexPtr;
    public final byte[] buffer;

    public Header(byte[] buff) {
        assert buff.length >= 16;
        version = Searcher.getInt2(buff, 0);
        indexPolicy = Searcher.getInt2(buff, 2);
        createdAt = Searcher.getInt(buff, 4);
        startIndexPtr = Searcher.getInt(buff, 8);
        endIndexPtr = Searcher.getInt(buff, 12);
        buffer = buff;
    }

    @Override public String toString() {
        return "{" +
                "Version: " + version + ',' +
                "IndexPolicy: " + indexPolicy + ',' +
                "CreatedAt: " + createdAt + ',' +
                "StartIndexPtr: " + startIndexPtr + ',' +
                "EndIndexPtr: " + endIndexPtr +
                '}';
    }
}
