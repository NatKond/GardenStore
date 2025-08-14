package de.telran.gardenStore.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && " +
            "execution(* *..*.*(..)) && " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMethods() {}

    @Before("postMethods()")
    public void logPostMethodInput(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info(">>> POST request parameters {}.{}(): {}",
                className, methodName, Arrays.toString(args));


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.info("User: {}, Roles: {}",
                    authentication.getName(),
                    authentication.getAuthorities());
        }
    }

    @AfterReturning(pointcut = "postMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("<<< Execution result {}: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }
}