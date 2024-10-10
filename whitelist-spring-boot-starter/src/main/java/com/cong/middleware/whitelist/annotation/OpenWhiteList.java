package com.cong.middleware.whitelist.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface OpenWhiteList {

    /**
     * 钥匙
     *
     * @return {@link String }
     */
    String key() default "";

    /**
     * 返回结果
     *
     * @return {@link String }
     */
    String returnResult() default "";
}
