package de.telran.gardenStore.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *) && " +
            "(execution(* *..*.*(..)) && " +
            "(@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) || " +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)))")
    public void controllerMethods() {
    }
    private String filterSensitiveData(Object obj) {
        if (obj == null) return "null";

        try {
            ObjectMapper mapper = new ObjectMapper();
            FilterProvider filters = new SimpleFilterProvider()
                    .addFilter("sensitiveDataFilter",
                            SimpleBeanPropertyFilter.serializeAllExcept("password", "token", "secretKey"));
            return mapper.writer(filters).writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
    }
    @Before("controllerMethods()")
    public void logMethodInput(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String httpMethod = "UNKNOWN";
        if (method.isAnnotationPresent(GetMapping.class)) {
            httpMethod = "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            httpMethod = "POST";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            httpMethod = "PUT";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            httpMethod = "DELETE";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            httpMethod = "PATCH";
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            httpMethod = Arrays.toString(requestMapping.method());
        }

        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        Object[] filteredArgs = Arrays.stream(args)
                .map(this::filterSensitiveData)
                .toArray();
        logger.info(">>> [{}] {}.{}() - Args: {}",
                httpMethod, className, methodName, filteredArgs);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.info("User: {}, Roles: {}",
                    authentication.getName(),
                    authentication.getAuthorities());
        }
    }

    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        logger.info("<<< {} - Returned: {}",
                joinPoint.getSignature().toShortString(),
                result);
    }
}