package com.zhglxt.activiti.config;

import com.zhglxt.activiti.service.ext.CustomGroupEntityManagerFactory;
import com.zhglxt.activiti.service.ext.CustomUserEntityManagerFactory;
import org.activiti.engine.*;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.image.impl.DefaultProcessDiagramGenerator;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * 工作流相关配置
 * @author liuwy
 */
@Component
@ConfigurationProperties(prefix = "activiti")
public class ActivitiConfig {
    private String dataType;

    private String formServerUrl;

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getFormServerUrl() {
        return formServerUrl;
    }

    public void setFormServerUrl(String formServerUrl) {
        this.formServerUrl = formServerUrl;
    }

    @Autowired
    private CustomUserEntityManagerFactory customUserEntityManagerFactory;

    @Autowired
    private CustomGroupEntityManagerFactory customGroupEntityManagerFactory;

    /**
     * 流程配置，与spring整合采用SpringProcessEngineConfiguration这个实现
     * @param dataSource
     * @param transactionManager
     * @return
     */
    @Bean
    public ProcessEngineConfiguration processEngineConfiguration(DataSource dataSource,
                                                                 PlatformTransactionManager transactionManager) {
        SpringProcessEngineConfiguration processEngineConfiguration = new SpringProcessEngineConfiguration();
        processEngineConfiguration.setDataSource(dataSource);
        /**
         * public static final String DB_SCHEMA_UPDATE_FALSE = "false";操作activiti23张表的时候，如果表不存在，就抛出异常，不能自动创建23张表
         *
         * public static final String DB_SCHEMA_UPDATE_CREATE_DROP = "create-drop";每次操作，都会先删除表，再创建表
         *
         * public static final String DB_SCHEMA_UPDATE_TRUE = "true";如果表不存在，就创建表，如果表存在，就直接操作
         */
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);

        //指定数据库
        processEngineConfiguration.setDatabaseType(dataType);

        processEngineConfiguration.setTransactionManager(transactionManager);

        // 流程图字体
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");

        /**
         * 	这段代码表示是否使用activiti自带用户组织表，如果是，这里为true，如果不是，这里为false。
         * 	由于本项目使用了视图的方式代替了原有的用户组织表，所以这里设置为false，这样启动就不用去检查用户组织表是否存在。
         *  */
        processEngineConfiguration.setDbIdentityUsed(true);

        //历史记录保存模式
        processEngineConfiguration.setHistory("full");

        processEngineConfiguration.setProcessDiagramGenerator(new DefaultProcessDiagramGenerator());

        //自定义用户和组
        List<SessionFactory> customSessionFactories = new ArrayList<>();
        customSessionFactories.add(customUserEntityManagerFactory);
        customSessionFactories.add(customGroupEntityManagerFactory);
        processEngineConfiguration.setCustomSessionFactories(customSessionFactories);

        return processEngineConfiguration;
    }

    /**
     * 流程引擎，与spring整合使用factoryBean
     * @param processEngineConfiguration
     * @return
     */
    @Bean
    public ProcessEngineFactoryBean processEngine(ProcessEngineConfiguration processEngineConfiguration) {
        ProcessEngineFactoryBean processEngineFactoryBean = new ProcessEngineFactoryBean();
        processEngineFactoryBean.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);
        return processEngineFactoryBean;
    }

    /**
     * 八大接口
     */
    @Bean
    public RepositoryService repositoryService(ProcessEngine processEngine) {
        return processEngine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine processEngine) {
        return processEngine.getRuntimeService();
    }

    @Bean
    public TaskService taskService(ProcessEngine processEngine) {
        return processEngine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine processEngine) {
        return processEngine.getHistoryService();
    }

    @Bean
    public FormService formService(ProcessEngine processEngine) {
        return processEngine.getFormService();
    }

    @Bean
    public IdentityService identityService(ProcessEngine processEngine) {
        return processEngine.getIdentityService();
    }

    @Bean
    public ManagementService managementService(ProcessEngine processEngine) {
        return processEngine.getManagementService();
    }

    @Bean
    public DynamicBpmnService dynamicBpmnService(ProcessEngine processEngine) {
        return processEngine.getDynamicBpmnService();
    }

}
