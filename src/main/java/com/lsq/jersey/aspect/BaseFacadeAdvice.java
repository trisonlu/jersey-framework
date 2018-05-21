package com.lsq.jersey.aspect;

import com.lsq.jersey.api.response.Response;
import com.lsq.jersey.api.response.ResponseStatusEnum;
import com.lsq.jersey.util.ValidateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by trison on 2018/5/21.
 */
@Aspect
@Slf4j
@Component
public class BaseFacadeAdvice {
    @Around("execution(* com.lsq.jersey.facade.*.*(..))")
    public Object baseAroundAdvice(ProceedingJoinPoint point) throws Throwable{
        Object args[] = point.getArgs();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        long startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
        long endTimeMillis = 0; // 记录方法结束执行的时间

        log.info("before->{}.{} : {} ",method.getDeclaringClass().getName(), method.getName(), StringUtils.join(args," ; "));

        for (Object arg:args) {
            List<String> errorList = ValidateUtil.validate(arg);
            if(!CollectionUtils.isEmpty(errorList)) {
                log.error("{}", errorList);
                Response response = Response.renderResponse(ResponseStatusEnum.REQUEST_ERROR, errorList.toString());
                endTimeMillis = System.currentTimeMillis(); // 记录方法结束执行的时间
                response.setCostTime(endTimeMillis - startTimeMillis);
                log.info("after->{}.{} : {} ",method.getDeclaringClass().getName(), method.getName(), StringUtils.join(args," ; "));
                log.info("costTime:{}ms, return value:{}", endTimeMillis - startTimeMillis, response);
                return response;
            }
        }

        Object returnValue = point.proceed(args);
        endTimeMillis = System.currentTimeMillis(); // 记录方法结束执行的时间
        log.info("after->{}.{} : {} ",method.getDeclaringClass().getName(), method.getName(), StringUtils.join(args," ; "));
        log.info("costTime:{}ms, return value:{}", endTimeMillis - startTimeMillis, returnValue);
        if (returnValue != null && returnValue instanceof Response) {
            Response response = (Response) returnValue;
            response.setCostTime(endTimeMillis - startTimeMillis);
            return response;
        }
        return returnValue;
    }
}
