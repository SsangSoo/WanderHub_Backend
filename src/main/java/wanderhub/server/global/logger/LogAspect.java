//package wanderhub.server.global.logger;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import wanderhub.server.global.audit.Auditable;
//
//import java.lang.reflect.Method;
//
//@Aspect             // 횡단관심사 적용.
//@Slf4j              // 로그
////@Profile("local")   // yml의 local 선택
//@Component
//public class LogAspect {
//
//    // 해당 프로젝트 하위 모든 메서드 시간 측정시 사용. => logging과 연결
//    @Pointcut("execution(* wanderhub.server..*(..))")
//    public void all() {}
//
//
//    // Controller와 Service 실행시 나타나는 메서드의 정보를 나타냄. => beforeLogic과 연결
//    // 'Controller'로 끝나는 클래스의 모든 메서드 실행시
//    @Pointcut("execution(* wanderhub.server..*Controller.*(..))")
//    public void controller() {}
//
//    // 'Service'로 끝나는 클래스의 모든 메서드 실행시
//    @Pointcut("execution(* wanderhub.server.*Service.*(..))")
//    public void service(){}
//
//
//    // 메서드 호출 이전 이후 예외 발생등 모든 시점에서 동작
//    // 시간 측정
//    @Around("all()")
//    public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
//        long start = System.currentTimeMillis();
//        try {
//            return joinPoint.proceed(); // 매서드의 실행한 후의 결과 값 return
//        } finally {
//            long finish = System.currentTimeMillis();
//            long timeMs = finish - start;
//            log.info("log = {}" , joinPoint.getSignature());    // 메서드에 대한 정보
//            log.info("timeMs = {}", timeMs);                    // 실행 시간.
//        }
//    }
//
//    // Controller / service의 호출된 메서드 정보를 얻어와서 log로 표시
//    @Before("controller() || service()")
//    public void beforeLogic(JoinPoint joinPoint) throws Throwable {
//        // 메서드 정보를 나타낸다.
//        // MethodSignature => 메서드 정보
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        log.info("method = {}", method.getName());  // 호출한 메서드 이름
//
//        Object[] args = joinPoint.getArgs();    // 매개변수들을 얻어온다.
//        for (Object arg : args) {
//            if(arg != null) {
//                // 매개변수 타입
//                log.info("type = {}", arg.getClass().getSimpleName());
//                // 매개변수 이름
//                log.info("value = {}", arg);
//            }
//        }
//    }
//}