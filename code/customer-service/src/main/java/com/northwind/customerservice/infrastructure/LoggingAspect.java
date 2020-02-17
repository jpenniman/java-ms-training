package com.northwind.customerservice.infrastructure;

import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class LoggingAspect {

    @AfterThrowing(value = "@annotation(com.northwind.customerservice.infrastructure.Loggable)", throwing = "ex")
    //@AfterThrowing(value = "execution(* com.northwind.repositories.*.*(..))", throwing = "ex")
    public void afterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Loggable loggable = method.getAnnotation(Loggable.class);
        String message;
        if (loggable.message() == null || loggable.message().trim().length() == 0)
            message = "An error occurred.";
        else
            message = loggable.message();

        LogFactory.getLog(joinPoint.getTarget().getClass()).error(message, ex);
    }

//    @Around("execution....")
//    public void aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
//        // execute before...
//
//        // next..
//        Object restult = joinPoint.proceed(joinPoint.getArgs());
//
//        //execute after
//
//    }
}
