package de.telran.gardenStore.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("@annotation(de.telran.gardenStore.annotation.Loggable)")
    public void loggableMethods() {
    }

    @Before("loggableMethods()")
    public void logMethodInput(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info(">>> POST request parameters {}.{}(): {}",
                className, methodName, Arrays.toString(args));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("User: {}, Roles: {}",
                    authentication.getName(),
                    authentication.getAuthorities());
        }
    }

    @AfterReturning(pointcut = "loggableMethods()", returning = "result")
    public void logMethodOutput(JoinPoint joinPoint, Object result) {
        log.info("<<< Execution result {}: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }
}