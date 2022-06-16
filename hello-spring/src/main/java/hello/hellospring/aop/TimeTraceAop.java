package hello.hellospring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

// AOP는 Aspect를 붙여줘야 한다.
// 그리고 Bean에 등록해야 하므로 @Component를 붙여주거나 설정 파일에 추가해도 된다.
@Component
@Aspect
public class TimeTraceAop {

    // Around로  대상을 지정해줄 수 있다.
    // 아래 어노테이션은 일단 hellospring 패키지 아래에 있는 모든 것에 다 적용
    @Around("execution(* hello.hellospring..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("START: " + joinPoint.toString());
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joinPoint.toString() + " " + timeMs + "ms");
        }
    }
}
