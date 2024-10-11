package com.cong.middleware.hystrix;

import com.cong.middleware.hystrix.valve.impl.HystrixValveImpl;
import com.cong.middleware.hystrix.annotation.OpenHystrix;
import com.cong.middleware.hystrix.valve.IValveService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@ComponentScan(basePackages = "com.cong.middleware.*")
public class OpenHystrixPoint {

    @Pointcut("@annotation(com.cong.middleware.hystrix.annotation.OpenHystrix)")
    public void aopPoint() {
    }

    @Around("aopPoint() && @annotation(doGovern)")
    public Object doRouter(ProceedingJoinPoint jp, OpenHystrix doGovern) throws Throwable {
        IValveService valveService = new HystrixValveImpl(doGovern, jp, getMethod(jp), jp.getArgs());
        return valveService.access();
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

}
