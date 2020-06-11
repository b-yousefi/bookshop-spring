package b_yousefi.bookshop.aop;

import b_yousefi.bookshop.models.User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

/**
 * Created by: b.yousefi
 * Date: 5/15/2020
 */
@Aspect
@Transactional
@Component
@Slf4j
public class FilterProjectsAspect {

//    @Pointcut("execution(*  b_yousefi.bookshop.jpa.*.findAll(..))")
//    public void projectFindAll() {
//    }
//
//    @Around("projectFindAll()")
//    public Object filterProjectsByUser(final ProceedingJoinPoint pjp) throws Throwable {
//        Authentication authentication =
//                SecurityContextHolder.getContext().getAuthentication();
//        User user = (User) authentication.getPrincipal();
//        //ByUser for authenticated user or for all in case the user has ROLE_ADMIN
//        //((ReflectiveMethodInvocation) ((MethodInvocationProceedingJoinPoint) pjp).methodInvocation).method.clazz.getSimpleName();
//        if (user != null) {
//            if(user.isAdmin()){
//                pjp.proceed(pjp.getArgs());
//            } else {
//                //throw new IllegalAccessException("can't access data without filtering with id");
////                Object[] args = pjp.getArgs();
////                for (int i = 0; i < args.length; i++) {
//////            if (args[i] instanceof Predicate) {
//////                Predicate predicate=(Predicate) args[i];
//////                BooleanExpression isProjectOwner =buildExpressionForUser()
//////                predicate = ExpressionUtils.allOf(isProjectOwner, predicate);
//////                args[i]=predicate;  //Update args
//////            }
////
////                    if (args[i] instanceof String) {
////                        String storer = (String) args[i];
////                        // Find storer by user
////                        // if(user.getAuthorities().contains()) not admin
////                        if (!user.getRole().equals("ROLE_ADMIN"))
////                            args[i] = user.getUsername();  //Update args
////                    }
////                }
//            }
//        }
//
//
//
//        //return pjp.proceed(args);
//    }
}