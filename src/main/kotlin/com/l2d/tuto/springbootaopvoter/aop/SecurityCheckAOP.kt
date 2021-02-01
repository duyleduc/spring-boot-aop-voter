package com.l2d.tuto.springbootaopvoter.aop

import com.l2d.tuto.springbootaopvoter.SecurityUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.ConfigAttribute
import java.lang.reflect.Method

/**
 *
 * Class to implement AOP to check your data's permission before entering into controller
 * It will be activated when you have the annotaion @AliceCustomCheckArg in your controller's parameters
 *
 */
@Aspect
class SecurityCheckAOP {
    @Autowired
    lateinit var adm: AccessDecisionManager

    @Pointcut("execution (* com.l2d.tuto..*.*(.., @com.l2d.tuto.springbootaopvoter.aop.AliceCustomCheckArg (*), ..))")
    fun checkSecurityCheckArg() {
    }

    @Before("checkSecurityCheckArg()")
    fun checkMethod(joinPoint: JoinPoint) {
        println("check securitycheckarg")
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        var method: Method = signature.method

        if (method.declaringClass.isInterface) {
            method = joinPoint.target.javaClass.getDeclaredMethod(joinPoint.signature.name, *method.parameterTypes)
        }

        val configAttributes = mutableListOf<ConfigAttribute>()
        method.parameterAnnotations.forEachIndexed { index, it ->
            it.forEach { a ->
                run {
                    if (a is AliceCustomCheckArg) {
                        val sca: AliceCustomCheckArg = a
                        val monAttribute = AliceConfigAttribute()
                        monAttribute.entity = sca.value
                        monAttribute.value = joinPoint.args[index]

                        configAttributes.add(monAttribute)
                    }
                }
            }
        }

        if (configAttributes.isNotEmpty()) {
            SecurityUtils.getContext()?.authentication?.let {
                adm.decide(it, joinPoint, configAttributes)
            }
        }
    }
}