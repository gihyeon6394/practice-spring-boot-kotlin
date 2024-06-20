package com.practice.kopring.application.tmp

import org.springframework.stereotype.Component

/**
 * @author gihyeon-kim
 */
@Component
class ChildBean1 : ParentBean   {
    override fun print() {
        println("ChildBean1")
    }

}
