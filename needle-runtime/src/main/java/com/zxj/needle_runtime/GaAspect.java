package com.zxj.needle_runtime;

import com.zxj.needle_annotations.GaAnnotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class GaAspect {
    @Pointcut("execution(@com.zxj.needle_annotations.GaAnnotation * *(..)) && @annotation(anno)")
    public void trackPoint(GaAnnotation anno){}

    @Before("trackPoint(anno)")
    public void track(JoinPoint joinPoint, GaAnnotation anno){
        Needle.getGaTracker().track(anno.category(),anno.action(),anno.label());
    }
}
