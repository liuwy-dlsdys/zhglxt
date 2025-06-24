package com.zhglxt.common.ipLocation;

import com.zhglxt.common.ipLocation.maker.IndexPolicy;
import com.zhglxt.common.ipLocation.maker.Log;
import com.zhglxt.common.ipLocation.maker.Maker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MakerMethod {

    public final static Log log = Log.getLogger(MakerMethod.class);
    public final static Pattern p = Pattern.compile("^(\\d+(-\\d+)?)$");

    public static void main(String[] args) {
        try {
            genDb();
        } catch (Exception e) {
            System.out.printf("运行 genDb 失败: %s\n", e);
        }
    }

    public static void genDb() throws Exception {
        // 源 IP 文本文件路径
        String srcFile = "zhglxt-common/src/main/java/com/zhglxt/common/ipLocation/data/ip.merge.txt";
        // 生成的 xdb 文件路径（把生成后的ip2region.xdb文件放到zhglxt-web/src/main/resources/ipdb目录下即可正常使用）
        String dstFile = "zhglxt-common/src/main/java/com/zhglxt/common/ipLocation/data/ip2region.xdb";

        String fieldList = "";
        String logLevel = "info";
        int indexPolicy = IndexPolicy.Vector;

        final int[] fields = getFieldList(fieldList);
        if (fields == null) {
            return;
        }

        long tStart = System.currentTimeMillis();
        final Maker maker = new Maker(indexPolicy, srcFile, dstFile, fields);
        log.infof("\nxdb生成到：\n src=%s,\n dst=%s,\n logLevel=%s", srcFile, dstFile, logLevel);
        Maker.log.setLevel(logLevel);
        maker.init();
        maker.start();
        maker.end();
        log.infof("xdb文件生成完毕, 耗时: %d s", (System.currentTimeMillis() - tStart) / 1000);
    }

    private static int[] getFieldList(String fieldList) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        final Map<String, String> map = new HashMap<String, String>();
        if (!fieldList.isEmpty()) {
            String[] fList =  fieldList.split(",");
            for (String f : fList) {
                final String s = f.trim();
                if (s.isEmpty()) {
                    log.errorf("未定义的选项 `%s`", f);
                    return null;
                }

                final Matcher m = p.matcher(s);
                if (!m.matches()) {
                    log.errorf("字段 `%s` 不是数字", f);
                    return null;
                }

                final String ms = m.group(1);
                if (ms.indexOf('-') == -1) {
                    if (map.containsKey(s)) {
                        log.errorf("重复字段索引 `%s`", s);
                        return null;
                    }
                    map.put(s, s);
                    final int idx = Integer.parseInt(s);
                    if (idx < 0) {
                        log.errorf("字段索引 `%s` 为负值", s);
                        return null;
                    }
                    list.add(idx);
                    continue;
                }

                // index range parse
                final String[] ra = ms.split("-");
                if (ra.length != 2) {
                    log.errorf("字段 `%s` 不是有效范围", ms);
                    return null;
                }

                final int start = Integer.parseInt(ra[0]);
                final int end = Integer.parseInt(ra[1]);
                if (start > end) {
                    log.errorf("索引范围开始 (%d) 应该  <= 结束 (%d)", start, end);
                    return null;
                }

                for (int i = start; i <= end; i++) {
                    final String _s = String.valueOf(i);
                    if (map.containsKey(_s)) {
                        log.errorf("重复字段索引 `%s`", _s);
                        return null;
                    }
                    map.put(_s, _s);
                    list.add(i);
                }
            }
        }

        final int[] fields = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            fields[i] = list.get(i);
        }

        Arrays.sort(fields);
        return fields;
    }
}