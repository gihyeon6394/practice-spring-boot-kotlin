package com.practice.kopring

import com.practice.kopring.application.member.Member
import com.practice.kopring.application.member.repo.MemberRepo
import com.practice.kopring.application.member.repo.TeamRepo
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class NPlusOneTest @Autowired constructor(
    private val teamRepo: TeamRepo,
    private val memRepo: MemberRepo,
    private val em: EntityManager,
) {


    @Test
    fun get1() {
        memRepo.findAllById(listOf(1, 2, 3, 4)).let {
            println("founded")
        }
    }

    @Test
    fun getBeFetch() {
        em.createQuery("select m from Member m join fetch m.team", Member::class.java).resultList.forEach {
            println(it)
        }
    }
}
