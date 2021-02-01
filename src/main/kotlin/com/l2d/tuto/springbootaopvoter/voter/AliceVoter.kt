package com.l2d.tuto.springbootaopvoter.voter

import com.l2d.tuto.springbootaopvoter.aop.AliceConfigAttribute
import org.springframework.core.GenericTypeResolver
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.security.access.ConfigAttribute
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import kotlin.streams.toList

/**
 * Abstract voter to check the data's permisison
 */
abstract class AliceVoter<T> : AccessDecisionVoter<T> {
    var entityType: Class<T>

    constructor() {
        this.entityType = voteEntityType()
    }

    abstract fun voteEntity(config: AliceConfigAttribute): Int

    override fun vote(authentication: Authentication?, `object`: T, attributes: MutableCollection<ConfigAttribute>?): Int {
        if (authentication!!.principal !is UserDetails) {
            return AccessDecisionVoter.ACCESS_DENIED
        }
        val supportedAttributes = attributes!!.parallelStream()
                .filter { supports(it) }
                .toList()

        return if (supportedAttributes.isNotEmpty()) specifyVote(supportedAttributes) else AccessDecisionVoter.ACCESS_ABSTAIN
    }

    override fun supports(attribute: ConfigAttribute?): Boolean {
        if (attribute is AliceConfigAttribute) {
            return attribute.entity!!.java.name == this.entityType.name
        }
        return false
    }

    override fun supports(clazz: Class<*>?): Boolean {
        return true
    }

    protected fun getIds(config: AliceConfigAttribute): MutableList<Int> {
        return if (config.value == null) ArrayList()
        else if (config.value is Array<*>) {
            val arr: Array<Int> = config.value as Array<Int>
            arr.asList().toMutableList()
        } else if (config.value is ArrayList<*>) (config.value as List<Int>).toMutableList()
        else {
            if (config.value is String) {
                listOf((config.value as String).toInt()).toMutableList()
            } else {
                listOf(config.value as Int).toMutableList()
            }
        }
    }

    private fun voteEntityType(): Class<T> {
        val types: List<Class<*>> = GenericTypeResolver.resolveTypeArguments(this.javaClass, AliceVoter::class.java)!!.toList()
        return types[0] as Class<T>
    }

    private fun specifyVote(configAttributes: List<ConfigAttribute>): Int {
        var granted = 0
        var denied = 0
        var abstain = 0


        // You could specify your algo to verify your data here
        // For example: you could get user's authentication information and other's security infos.
        configAttributes.forEach {
            when (voteEntity(it as AliceConfigAttribute)) {
                AccessDecisionVoter.ACCESS_ABSTAIN -> {
                    abstain++
                }
                AccessDecisionVoter.ACCESS_DENIED -> {
                    denied++
                }
                else -> {
                    granted++
                }
            }
        }

        if (denied > 0) {
            return AccessDecisionVoter.ACCESS_DENIED
        }

        return if (granted > abstain) {
            AccessDecisionVoter.ACCESS_GRANTED
        } else {
            AccessDecisionVoter.ACCESS_ABSTAIN
        }

    }
}
