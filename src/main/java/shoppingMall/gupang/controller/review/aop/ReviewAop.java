package shoppingMall.gupang.controller.review.aop;

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

    @Pointcut("execution(* shoppingMall.gupang.controller.review.ReviewController.*(..))")
    private void pointCut(){}

    private Long startTime;

    @Before("pointCut()")
    public void before(JoinPoint joinPoint) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        Object[] args = joinPoint.getArgs();
//        log.info("Method name={}", method.getName());
        startTime = System.currentTimeMillis();

//        for (Object arg : args) {
//            log.info("type={}", arg.getClass().getSimpleName());
//            log.info("value={}", arg);
//        }
    }

    @AfterReturning(value = "pointCut()", returning = "obj")
    public void afterReturning(JoinPoint joinPoint, Object obj) {

//        log.info((String) obj);
        long endTime = System.currentTimeMillis();

        if ((endTime-startTime) >= 50) {
            log.warn(String.valueOf(endTime-startTime));
        }

    }
}
