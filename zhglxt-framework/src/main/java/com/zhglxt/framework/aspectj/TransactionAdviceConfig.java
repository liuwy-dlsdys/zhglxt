package com.zhglxt.framework.aspectj;


import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * Springboot aop 统一事务处理
 *
 * 1.springboot实现事务只需要在头上加上@Transactional注解
 *  @Transactional 默认只捕获RuntimeException.class
 * 对Exception异常得需要 @Transactional(rollbackFor = {Exception.class}) 捕获回滚。
 *
 * 2.当项目较大时，对所有的service都加上事务，显得比较麻烦。所以通过aop方式实现全局事务处理。专注写业务逻辑，注意下方法名命名即可。
 * 可以使用 source.addTransactionalMethod("*", txAttr_REQUIRED), 匹配所有方法。但是并不是所有的方法都需要事务。所以推荐使用匹配方法名的方法
 *
 * @author liuwy
 * @version 2019/10/9
 */
@Aspect
@Component
public class TransactionAdviceConfig {

    /**
     * 多个切入点表达式使用 and
     */
//	private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.zhglxt.*.service.impl.*.*(..)) and execution(* com.zhglxt.activiti.service.*.*(..))";
    private static final String AOP_POINTCUT_EXPRESSION = "execution(* com.zhglxt.*.service.impl.*.*(..))";

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public TransactionInterceptor txAdvice() {

        /**
         * 事务有以下几个常用属性：
         *
         * read-only:设置该事务中是否允许修改数据。（对于只执行查询功能的事务，设置为TRUE可以提高事务的执行速度）
         *
         * propagation：事务的传播机制。一般设置为required。可以保证在事务中的代码只在当前事务中运行，防止创建多个事务。
         *
         * isolation:事务隔离级别。不是必须的。默认值是default。
         *
         * timeout:允许事务运行的最长时间，以秒为单位。
         *
         * rollback-for:触发回滚的异常。
         *
         * no-rollback-for:不会触发回滚的异常。
         *
         * 实际开发中，对于只执行查询功能的事务，要设置read-only为TRUE，其它属性一般使用默认值即可
         **/
        //事务的传播行为
        DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute();
        txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

        //只读事务
        DefaultTransactionAttribute txAttr_REQUIRED_READONLY = new DefaultTransactionAttribute();
        txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        txAttr_REQUIRED_READONLY.setReadOnly(true);

        NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();

        source.addTransactionalMethod("remove*", txAttr_REQUIRED);
        source.addTransactionalMethod("update*", txAttr_REQUIRED);
        source.addTransactionalMethod("delete*", txAttr_REQUIRED);
        source.addTransactionalMethod("insert*", txAttr_REQUIRED);
        source.addTransactionalMethod("create*", txAttr_REQUIRED);
        source.addTransactionalMethod("clean*", txAttr_REQUIRED);
        source.addTransactionalMethod("add*", txAttr_REQUIRED);
        source.addTransactionalMethod("save*", txAttr_REQUIRED);
        source.addTransactionalMethod("batch*", txAttr_REQUIRED);
        source.addTransactionalMethod("edit*", txAttr_REQUIRED);
        source.addTransactionalMethod("do*", txAttr_REQUIRED);
        source.addTransactionalMethod("import*", txAttr_REQUIRED);
        source.addTransactionalMethod("run*", txAttr_REQUIRED);

        source.addTransactionalMethod("complete*", txAttr_REQUIRED);
        source.addTransactionalMethod("start*", txAttr_REQUIRED);
        source.addTransactionalMethod("myDeploy*", txAttr_REQUIRED);
        source.addTransactionalMethod("deploy*", txAttr_REQUIRED);
        source.addTransactionalMethod("audit*", txAttr_REQUIRED);
        source.addTransactionalMethod("claim*", txAttr_REQUIRED);
        source.addTransactionalMethod("convert*", txAttr_REQUIRED);

        /*使用以下字符开头命名的方法,开启只读模式,提高数据库访问性能*/
        source.addTransactionalMethod("get*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("query*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("list*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("count*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("select*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("load*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("search*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("valid*", txAttr_REQUIRED_READONLY);
        source.addTransactionalMethod("*", txAttr_REQUIRED_READONLY);

        return new TransactionInterceptor(transactionManager, source);
    }

    @Bean
    public Advisor txAdviceAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
        return new DefaultPointcutAdvisor(pointcut, txAdvice());
    }
}
