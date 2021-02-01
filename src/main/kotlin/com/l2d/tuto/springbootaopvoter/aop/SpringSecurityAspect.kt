package com.l2d.tuto.springbootaopvoter.aop

import com.l2d.tuto.springbootaopvoter.voter.AliceVoter
import com.l2d.tuto.springbootaopvoter.voter.TestVoter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.security.access.AccessDecisionManager
import org.springframework.security.access.vote.UnanimousBased


@Configuration
@EnableAspectJAutoProxy
class SpringSecurityAspect {

    @Autowired
    lateinit var testVoter: TestVoter

    @Bean
    fun securityCheckAOP(): SecurityCheckAOP {
        return SecurityCheckAOP()
    }

    @Bean
    fun adManager(): AccessDecisionManager {
        val ub = UnanimousBased(listVoters())
        ub.isAllowIfAllAbstainDecisions = false
        return ub
    }

    fun listVoters(): List<AliceVoter<*>> {
        val list: MutableList<AliceVoter<*>> = ArrayList()
        list.add(testVoter)
        return list
    }
}
