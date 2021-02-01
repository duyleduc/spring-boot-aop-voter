package com.l2d.tuto.springbootaopvoter.voter

import com.l2d.tuto.springbootaopvoter.aop.AliceConfigAttribute
import com.l2d.tuto.springbootaopvoter.dto.TestDTO
import org.springframework.security.access.AccessDecisionVoter
import org.springframework.stereotype.Component

@Component
class TestVoter : AliceVoter<TestDTO>() {
    override fun voteEntity(config: AliceConfigAttribute): Int {
        val ids = getIds(config)

        if (ids.size == 1) {
            if (ids.get(0) == 100) {
                return AccessDecisionVoter.ACCESS_GRANTED
            }
        }
        return AccessDecisionVoter.ACCESS_DENIED
    }
}