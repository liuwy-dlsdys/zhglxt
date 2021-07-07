package com.zhglxt.demo.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author liuwy
 * @Date 2020/5/11
 */
@Component
@ConfigurationProperties(prefix = "pay.alipay")
public class AlipayConfig {

    private static String appid;
    private static String privateKey;
    private static String publicKey;
    private static String returnUrl;
    private static String notifyUrl;
    private static String signType;
    private static String charset;
    private static String logPath;
    private static String gatewayUrl;

    public static String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        AlipayConfig.appid = appid;
    }

    public static String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        AlipayConfig.privateKey = privateKey;
    }

    public static String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        AlipayConfig.publicKey = publicKey;
    }

    public static String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        AlipayConfig.returnUrl = returnUrl;
    }

    public static String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        AlipayConfig.notifyUrl = notifyUrl;
    }

    public static String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        AlipayConfig.signType = signType;
    }

    public static String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        AlipayConfig.charset = charset;
    }

    public static String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        AlipayConfig.logPath = logPath;
    }

    public static String getGatewayUrl() {
        return gatewayUrl;
    }

    public void setGatewayUrl(String gatewayUrl) {
        AlipayConfig.gatewayUrl = gatewayUrl;
    }
}
