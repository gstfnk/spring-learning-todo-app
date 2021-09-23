package io.github.gstfnk.aspect;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogicAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogicAspect.class);
    private final Timer projectCreateGroupTimer;

    LogicAspect(final MeterRegistry registry) {
        projectCreateGroupTimer = registry.timer("logic.project.create.group");
    }

    @Pointcut("execution(* io.github.gstfnk.logic.ProjectService.createGroup(..))")
    static void projectServiceCreateGroup() {
    }

    @Before("projectServiceCreateGroup()")
    void logMethodCall(JoinPoint joinPoint) {
        logger.info("Before {} with {}",
                joinPoint.getSignature().getName(),
                joinPoint.getArgs());
    }

    @Around("projectServiceCreateGroup()")
    Object aroundProjectCreateGroup(ProceedingJoinPoint proceedingJoinPoint) {
        return projectCreateGroupTimer.record(() -> {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw (RuntimeException) e;
                }
                throw new RuntimeException(e);
            }
        });
    }
}
