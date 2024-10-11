package com.cong.middleware.hystrix.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 打开 hystrix
 *
 * @author cong
 * @date 2024/10/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenHystrix {
    /**
     * 超时熔断返回结果
     *
     * @return {@link String }
     */
    String returnResult() default "";

    /**
     * 超时熔断值
     *
     * @return int
     */
    int timeoutValue() default 0;

}