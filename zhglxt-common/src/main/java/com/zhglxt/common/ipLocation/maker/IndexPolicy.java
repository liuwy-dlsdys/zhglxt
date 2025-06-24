package com.zhglxt.common.ipLocation.maker;

public class IndexPolicy {
    public static final int Vector = 1;
    public static final int BTree = 2;

    // parser the index policy from string
    public static int parse(String policy) throws Exception {
        String v = policy.toLowerCase();
        if ("vector".equals(v)) {
            return Vector;
        } else if ("btree".equals(v)) {
            return BTree;
        } else {
            throw new Exception("unknown index policy `"+policy+"`");
        }
    }
}