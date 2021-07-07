package com.zhglxt.common.util.uuid;

import com.zhglxt.common.util.Encodes;

import java.security.SecureRandom;

/**
 * 封装各种生成唯一性ID算法的工具类.
 *
 * @author liuwy
 * @date 2020-07-17
 */
public class IdUtils {

    private static SecureRandom random = new SecureRandom();

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static long randomLong() {
        return Math.abs(random.nextLong());
    }

    /**
     * 基于Base62编码的SecureRandom随机生成bytes.
     */
    public static String randomBase62(int length) {
        byte[] randomBytes = new byte[length];
        random.nextBytes(randomBytes);
        return Encodes.encodeBase62(randomBytes);
    }

    //==========================================================

    /**
     * 获取随机UUID
     *
     * @return 随机UUID
     */
    public static String randomUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String simpleUUID() {
        return UUID.randomUUID().toString(true);
    }

    /**
     * 获取随机UUID，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 随机UUID
     */
    public static String fastUUID() {
        return UUID.fastUUID().toString();
    }

    /**
     * 简化的UUID，去掉了横线，使用性能更好的ThreadLocalRandom生成UUID
     *
     * @return 简化的UUID，去掉了横线
     */
    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }


    public static void main(String[] args) {
        System.out.println(IdUtils.simpleUUID());
        System.out.println(IdUtils.simpleUUID().length());
        for (int i = 0; i < 1000; i++) {
            System.out.println(IdUtils.randomLong() + "  " + IdUtils.randomBase62(5));
        }
    }

}
