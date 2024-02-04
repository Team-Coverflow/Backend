package com.coverflow.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Component
@Aspect
public class LogAspect {

//    @Pointcut("execution(* com.coverflow..*(..))")
//    public void all() {
//    }

    @Pointcut("execution(* com.coverflow..*Controller.*(..))")
    public void controller() {
    }

    @Pointcut("execution(* com.coverflow..*Service.*(..))")
    public void service() {
    }

//    @Around("all()")
//    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//        try {
//            return joinPoint.proceed();
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            log.info("log = {}", joinPoint.getSignature());
//            log.info("timeMs = {}", timeMs);
//        }
//    }

    // 특정 조인포인트에서 수행될 부가기능을 정리
    @Before("controller() || service()")
    public void beforeLogic(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("====== method = {} ======", method.getName());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("parameter type = {}", arg.getClass().getSimpleName());
                log.info("parameter value = {}", arg);
            }

        }
    }

    @AfterReturning("controller() || service()")
    public void afterLogic(JoinPoint joinPoint) {
        Method method = getMethod(joinPoint);
        log.info("====== method = {} ======", method.getName());

        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                log.info("return type = {}", arg.getClass().getSimpleName());
                log.info("return value = {}", arg);
            }

        }
    }

    @AfterThrowing(pointcut = "controller()", throwing = "e")
    public void afterThrowingLogging(JoinPoint joinPoint, Exception e) {
        log.error("### Occured error in request {}", joinPoint.getSignature().toShortString());
        log.error("\t{}", e.getMessage());
    }

    // JoinPoint로 메서드 정보 가져오기
    private Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        return signature.getMethod();
    }
}
