package de.telran.gardenStore.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
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

    @Around(value = "loggableMethods()", argNames = "joinPoint")
    public Object logMethodInput(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        log.info(">>> Request parameters {}.{}(): {}",
                className, methodName, Arrays.toString(args));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            log.info("User: {}, Roles: {}",
                    authentication.getName(),
                    authentication.getAuthorities());
        }
        Object result = joinPoint.proceed(args);
        log.info("<<< Execution result {}: {}",
                joinPoint.getSignature().toShortString(),
                result);
        return result;
    }
}