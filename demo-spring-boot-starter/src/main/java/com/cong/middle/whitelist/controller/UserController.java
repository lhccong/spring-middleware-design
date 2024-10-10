package com.cong.middle.whitelist.controller;


import com.cong.middleware.whitelist.annotation.OpenWhiteList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    /**
     * 通过：http://localhost:8081/api/queryUserInfo?userId=aaa
     * 拦截：http://localhost:8081/api/queryUserInfo?userId=123
     */
    @OpenWhiteList(key = "userId", returnResult = "{\"code\":\"1111\",\"info\":\"不在白名单内，拦截！\"}")
    @GetMapping("/api/queryUserInfo")
    public UserInfo queryUserInfo(@RequestParam String userId) {
        logger.info("查询用户信息，userId：{}", userId);
        return new UserInfo("虫虫:" + userId, 19, "天津市东丽区万科赏溪苑14-0000");
    }

}

