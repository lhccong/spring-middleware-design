package com.cong.middleware.whitelist;

import com.alibaba.fastjson.JSON;
import com.cong.middleware.whitelist.annotation.OpenWhiteList;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 白名单方面
 *
 * @author cong
 * @date 2024/10/10
 */
@Aspect
@Component
public class WhiteListAspect {

    private final Logger logger = LoggerFactory.getLogger(WhiteListAspect.class);

    @Resource
    private String whiteListConfig;

    @Pointcut("@annotation(com.cong.middleware.whitelist.annotation.OpenWhiteList)")
    public void aopPoint() {
    }

    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint jp) throws Throwable {
        Object[] args = jp.getArgs();
        // 获取内容
        Method method = getMethod(jp);
        OpenWhiteList whiteList = method.getAnnotation(OpenWhiteList.class);
        // 拦截的方法
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();

        // 获取所有的入参名称
        String[] parameterNames = methodSignature.getParameterNames();


        // 获取字段值
        String key = whiteList.key(); //userId
        String keyValue = getFiledValue(key, jp.getArgs());

        logger.info("白名单处理 method：{} value：{}", method.getName(), keyValue);


        if (null == keyValue || keyValue.isEmpty()) return jp.proceed();

        String[] split = whiteListConfig.split(",");
        for (int i = 0; i < args.length; i++) {
            String paramName = parameterNames[i];
            // 参数名相同，进行白名单过滤
            if (key.equals(paramName)) {
                // 白名单过滤
                for (String str : split) {
                    if (keyValue.equals(str)) {
                        return jp.proceed();
                    }
                }
            }
        }


        // 拦截
        return returnObject(whiteList, method);
    }

    // 返回对象
    private Object returnObject(OpenWhiteList openWhiteList, Method method) throws IllegalAccessException, InstantiationException {
        Class<?> returnType = method.getReturnType();
        String returnJson = openWhiteList.returnResult();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }

    private Method getMethod(JoinPoint jp) throws NoSuchMethodException {
        Signature sig = jp.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return jp.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }

    // 获取属性值
    private String getFiledValue(String filed, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (null == filedValue || filedValue.isEmpty()) {
                    filedValue = BeanUtils.getProperty(arg, filed);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return filedValue;
    }
}
