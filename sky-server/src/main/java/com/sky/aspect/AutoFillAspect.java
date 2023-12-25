package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    @Pointcut("@annotation(com.sky.annotation.AutoFill)")
    public void pt(){}

    @Before("pt()")
    public void Advice(JoinPoint joinPoint){
        log.info("开始进行公共字段的填充");

        // 获得操作类型insert还是update
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();
        // 获取方法里的参数（实体），为其属性获得
        Object[] args = joinPoint.getArgs();
        if(args == null && args.length == 0)
        {
            return;
        }
        Object object = args[0];
        // 准备赋值数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();
        // 根据不同操作类型对其赋值（反射）
        if(operationType == OperationType.INSERT){
            try {
                Method setCreateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setCreateTime.invoke(object, now);
                setCreateUser.invoke(object, currentId);
                setUpdateTime.invoke(object, now);
                setUpdateUser.invoke(object, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 赋值4个属性
        }else if(operationType == OperationType.UPDATE)
        {
            // 赋值2个属性
            try {
                Method setUpdateTime = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = object.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateTime.invoke(object, now);
                setUpdateUser.invoke(object, currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
