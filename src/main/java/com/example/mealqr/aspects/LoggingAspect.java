package com.example.mealqr.aspects;

import com.example.mealqr.exceptions.ApiException;
import com.example.mealqr.security.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    public static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    /**
     * Pointcut that matches all Web REST endpoints.
     */
    @Pointcut("execution(* com.example.mealqr.web.rest..*.*(..))")
    public void endpointPointcut() {
        // Method is empty as this is just a Pointcut, the implementations are in the advices.
    }

    @Around("endpointPointcut()")
    public Object logAroundEndpoints(ProceedingJoinPoint joinPoint) throws Throwable {
        String username = SecurityUtils.getCurrentUserLogin()//
                .getOrElse("Unauthenticated");

        log.info("User: {}. Enter: {}.{}() with arguments: {}",//
                username,//
                joinPoint.getSignature().getDeclaringTypeName(),//
                joinPoint.getSignature().getName(),//
                joinPoint.getArgs());

        try {
            ResponseEntity result = (ResponseEntity) joinPoint.proceed();
            log.info("User: {}. Exit: {}.{}(). Status: {}",//
                    username,//
                    joinPoint.getSignature().getDeclaringTypeName(),//
                    joinPoint.getSignature().getName(),//
                    result.getStatusCode());
            return result;
        } catch (ApiException apiException) {
            log.error("User: {}. ApiException \"{}\". In {}.{}(). With arguments: {}",
                    username,//
                    apiException.getMessage(),//
                    joinPoint.getSignature().getDeclaringTypeName(),//
                    joinPoint.getSignature().getName(),//
                    Arrays.toString(joinPoint.getArgs()));
            throw apiException;
        } catch (Exception e) {
            log.error("User: {}. Exception \"{}\". In {}.{}(). With arguments: {}",
                    username,//
                    e.getCause(),//
                    joinPoint.getSignature().getDeclaringTypeName(),//
                    joinPoint.getSignature().getName(),//
                    Arrays.toString(joinPoint.getArgs()));
            throw e;
        }
    }
}
