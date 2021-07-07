package com.zhglxt.framework.config.properties;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @Description 数据库密码回调方法
 * @Author liuwy
 * @Date 2021/3/19
 */
@Component
public class DbPasswordCallback extends DruidPasswordCallback {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbPasswordCallback.class);

    /**
     * @param properties 属性值列表
     * @return
     * @description:处理解密
     */
    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        String password = (String) properties.get("password");
        String publickey = (String) properties.get("publickey");
        try {
            String dbpassword = ConfigTools.decrypt(publickey, password);
            setPassword(dbpassword.toCharArray());
        } catch (Exception e) {
            LOGGER.error("Druid ConfigTools.decrypt", e);
        }
    }
}
