package com.l2d.tuto.springbootaopvoter.aop

import kotlin.reflect.KClass

/**
 * Annotation that you will inject to your controller parameter to control user's permission on data
 * @value : The class that you want to verify pre-entering data
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class AliceCustomCheckArg (val value: KClass<*>)
