package com.l2d.tuto.springbootaopvoter.controller

import com.l2d.tuto.springbootaopvoter.aop.AliceCustomCheckArg
import com.l2d.tuto.springbootaopvoter.dto.TestDTO
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/alice/test")
class TestController {

    /**
     * This method will check the content of dto before your controller is executed
     */
    @GetMapping(value = ["/{id}"])
    fun testDto(@PathVariable(value = "id") @AliceCustomCheckArg(value = TestDTO::class) id: Int): ResponseEntity<TestDTO> {
        return ResponseEntity.ok(TestDTO())
    }
}