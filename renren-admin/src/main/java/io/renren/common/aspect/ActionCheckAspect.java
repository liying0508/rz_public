package io.renren.common.aspect;

import io.renren.common.annotation.ActionCheck;
import io.renren.modules.security.service.ShiroService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.List;

@Aspect
@Component
public class ActionCheckAspect {

    @Autowired
    ShiroService shiroService;

    @Pointcut("@annotation(io.renren.common.annotation.ActionCheck)")
    public void dataActionCheck() {

    }

    @Around("dataActionCheck()")
    public void afterList(JoinPoint point){
        System.out.println("--------开始做资源的权限自定义认证，确认目标是否具有数据查看权限---------");


        //现通过请求获取当前用户的id
        ServletRequestAttributes requestAttributes
                = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        Long userId = shiroService.getByToken(request.getHeader("token")).getUserId();

        //获取当前方法
        MethodSignature signature = (MethodSignature) point.getSignature();

        Method method = signature.getMethod();


    }
}
