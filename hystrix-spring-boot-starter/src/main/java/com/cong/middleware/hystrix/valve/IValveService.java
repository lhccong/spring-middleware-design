package com.cong.middleware.hystrix.valve;

import com.cong.middleware.hystrix.annotation.OpenHystrix;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;


/**
 * iValve 服务
 *
 * @author cong
 * @date 2024/10/11
 */
public interface IValveService {

    /**
     * 访问
     *
     * @param jp        日本
     * @param method    方法
     * @param doHystrix do hystrix
     * @param args      参数
     * @return {@link Object }
     * @throws Throwable 可投掷
     */
    Object access(ProceedingJoinPoint jp, Method method, OpenHystrix doHystrix, Object[] args) throws Throwable;

}
