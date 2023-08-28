package com.example.studentmanagementsystem.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.mapping.Join;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;


@Aspect     //indicates that it's an aspect class that contains cross-cutting concerns to be applied to other parts of application
@Configuration
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    //intercepts method calls to method defined within the below package
    @Around("within(com.example.studentmanagementsystem.serviceimpl.StudentServiceImpl)")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        logger.info("Request for {}.{}() with arguments[s]={}",
                proceedingJoinPoint.getSignature().getDeclaringType(),      //class or interface
                proceedingJoinPoint.getSignature().getName(),               //method name
                Arrays.toString(proceedingJoinPoint.getArgs()));            //arguments

        Instant startTime=Instant.now();
        Object returnValue=proceedingJoinPoint.proceed();
        Instant finishTime=Instant.now();
        Long timeElapsed= Duration.between(startTime,finishTime).toMillis();
        logger.info("Response for {}.{} with Result ={}",
                proceedingJoinPoint.getSignature().getDeclaringType(),
                proceedingJoinPoint.getSignature().getName(),
                returnValue);
        logger.info("Time taken ="+new SimpleDateFormat("mm:ss:SSSS").format(new Date(timeElapsed)));
        return returnValue;
    }

    @Pointcut("within(com.example.studentmanagementsystem.serviceimpl.*)")
    public void applicationExceptionPackage(){
    }

    //intercepts method calls that result in an exception being thrown from methods mentioned in poincut
    @AfterThrowing(pointcut = "applicationExceptionPackage()",throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint,Throwable e){
        logger.error("Exception in {}.{} with cause={}, with message= {}",
                joinPoint.getSignature().getDeclaringType(),
                joinPoint.getSignature().getName(),
                e.getCause()!=null?e.getCause():e.getClass().getSimpleName(),
                e.getMessage()!=null?e.getMessage():"NULL"
        );
    }

}
