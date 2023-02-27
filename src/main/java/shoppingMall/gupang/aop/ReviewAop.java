package shoppingMall.gupang.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
public class ReviewAop {

    @Pointcut("execution(* shoppingMall.gupang.web.controller.review.ReviewController.*(..))")
    private void pointCut(){}

    private Long startTime;

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
        startTime = System.currentTimeMillis();
    }

    @AfterReturning(value = "pointCut()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {
        long endTime = System.currentTimeMillis();

        if ((endTime-startTime) >= 100) {
            log.warn(String.valueOf(endTime-startTime));
        }
    }
}
