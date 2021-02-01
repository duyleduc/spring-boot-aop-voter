package com.l2d.tuto.springbootaopvoter

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder

class SecurityContext(val authentication: Authentication)
object SecurityUtils {
    fun getContext(): SecurityContext? {
        SecurityContextHolder.getContext()?.authentication?.let { auth ->
            return SecurityContext(auth)
        }
        return null
    }
}
