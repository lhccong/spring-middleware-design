package com.cong.middle.hystrix.controller;


import com.cong.middleware.hystrix.annotation.OpenHystrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHystrixController {

    private final Logger logger = LoggerFactory.getLogger(UserHystrixController.class);

    /**
     * 通过：timeoutValue = 1350
     * 熔断：timeoutValue = 350
     */
    @OpenHystrix(timeoutValue = 350, returnResult = "{\"code\":\"1111\",\"info\":\"调用方法超过350毫秒，熔断返回！\"}")
    @GetMapping("/api/hystrix/queryUserInfo")
    public UserInfo queryUserInfo(@RequestParam String userId) throws InterruptedException {
        logger.info("查询用户信息，userId：{}", userId);
        Thread.sleep(1000);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }

}

