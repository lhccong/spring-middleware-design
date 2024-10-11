package com.cong.middleware.hystrix.valve;


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
     * @return {@link Object }
     * @throws Throwable 可投掷
     */
    Object access() throws Throwable;

}
