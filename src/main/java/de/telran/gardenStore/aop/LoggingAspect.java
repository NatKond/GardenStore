package de.telran.gardenStore.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.telran.gardenStore.serializer.SensitiveDataSerializer;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {
    private final ObjectMapper objectMapper;

    public LoggingAspect() {
        this.objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(new SensitiveDataSerializer());
        this.objectMapper.registerModule(module);
    }

    // iskluchaem metody s parolem i tokenom
    @Pointcut("@annotation(de.telran.gardenStore.annotation.Loggable) && " +
            "!execution(* *..*.*Password*(..)) && " +
            "!execution(* *..*.*Token*(..))")
    public void loggableSafeMethods() {}

    @Before("loggableSafeMethods()")
    public void logMethodInput(JoinPoint joinPoint) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String args = objectMapper.writeValueAsString(joinPoint.getArgs());
            log.info(">>> {} - args: {}", methodName, args);
        } catch (Exception e) {
            log.warn("Failed to log method input", e);
        }
    }

    @AfterReturning(pointcut = "loggableSafeMethods()", returning = "result")
    public void logMethodOutput(JoinPoint joinPoint, Object result) {
        try {
            String methodName = joinPoint.getSignature().getName();
            String resultJson = objectMapper.writeValueAsString(result);
            log.info("<<< {} - result: {}", methodName, resultJson);
        } catch (Exception e) {
            log.warn("Failed to log method result", e);
        }
    }
}
//izmeneno