package com.cong.middleware.hystrix.valve.impl;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.hystrix.annotation.OpenHystrix;
import com.cong.middleware.hystrix.valve.IValveService;
import com.netflix.hystrix.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;


/**
 * hystrix 阀门 Impl
 *
 * @author cong
 * @date 2024/10/11
 */
public class HystrixValveImpl extends HystrixCommand<Object> implements IValveService {

    private final Logger logger = LoggerFactory.getLogger(HystrixValveImpl.class);
    private final ProceedingJoinPoint jp;
    private final Method method;
    private final OpenHystrix openHystrix;

    public HystrixValveImpl(OpenHystrix openHystrix, ProceedingJoinPoint jp, Method method, Object[] args) {
        /*********************************************************************************************
         * 置HystrixCommand的属性
         * GroupKey：            该命令属于哪一个组，可以帮助我们更好的组织命令。
         * CommandKey：          该命令的名称
         * ThreadPoolKey：       该命令所属线程池的名称，同样配置的命令会共享同一线程池，若不配置，会默认使用GroupKey作为线程池名称。
         * CommandProperties：   该命令的一些设置，包括断路器的配置，隔离策略，降级设置，以及一些监控指标等。
         * ThreadPoolProperties：关于线程池的配置，包括线程池大小，排队队列的大小等
         *********************************************************************************************/
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GovernGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GovernKey"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GovernThreadPool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(openHystrix.timeoutValue())
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10))
        );
        this.openHystrix = openHystrix;
        this.jp = jp;
        this.method = method;

        logger.info("断路器 args：{}", args);

    }

    @Override
    public Object access() {
        logger.info("断路器启动！！！");
        return this.execute();
    }

    @Override
    protected Object run() {
        try {
            return jp.proceed();
        } catch (Throwable throwable) {
            return null;
        }
    }

    @Override
    protected Object getFallback() {
        return JSON.parseObject(openHystrix.returnResult(), method.getReturnType());
    }

}
