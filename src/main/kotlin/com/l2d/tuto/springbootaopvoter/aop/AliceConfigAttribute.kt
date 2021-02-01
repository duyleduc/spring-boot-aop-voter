package com.l2d.tuto.springbootaopvoter.aop

import org.springframework.security.access.ConfigAttribute
import kotlin.reflect.KClass

open class AliceConfigAttribute : ConfigAttribute {

    // You could add others attributes here
    var entity: KClass<*>? = null
    var value: Any? = null

    override fun getAttribute(): String {
        return this.attribute
    }
}
