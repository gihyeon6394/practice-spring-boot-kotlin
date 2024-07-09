package com.practice.kopring.application.blog

import com.practice.kopring.application.member.Member
import com.practice.kopring.application.member.Team
import com.practice.kopring.application.member.repo.MemberRepo
import com.practice.kopring.application.member.repo.TeamRepo
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import java.time.LocalDateTime

@SpringBootTest
@Transactional
class HHH000104Test @Autowired constructor(
    private val teamRepo: TeamRepo,
    private val memRepo: MemberRepo,
    private val em: EntityManager,
) {

    @BeforeEach
    fun setUp() {
        deleteAll()

        val now = LocalDateTime.now()
        val aespa = Team(
            name = "에스파",
            ymdtCre = now,
        )
        val karina = Member(
            name = "카리나",
            ymdtCre = now,
        )
        val winter = Member(
            name = "윈터",
            ymdtCre = now,
        )
        aespa.members = mutableListOf(karina, winter)

        val newJeans = Team(
            name = "뉴진스",
            ymdtCre = now,
        )
        val minzi = Member(
            name = "지수",
            ymdtCre = now,
        )
        val haerin = Member(
            name = "해린",
            ymdtCre = now,
        )
        val hani = Member(
            name = "하니",
            ymdtCre = now,
        )
        newJeans.members = mutableListOf(minzi, haerin, hani)
        teamRepo.saveAll(listOf(aespa, newJeans))
    }

    @AfterEach
    fun afterEach() {
        deleteAll()
    }

    fun deleteAll() {
        teamRepo.deleteAll()
        memRepo.deleteAll()
        em.flush()
        em.clear()
    }

    @Test
    @DisplayName("데이터 잘 들어 갔는가")
    fun test() {
        with(teamRepo.findAll()) {
            println(this)
            assert(size == 2)
        }
    }

    @Test
    @DisplayName("페이지로 2개의 데이터만 가져오기, HHH90003004 발생")
    fun getTop2() {
        val pageSize = 2

        em.createQuery("SELECT distinct t FROM Team t join fetch t.members m", Team::class.java)
            .setMaxResults(pageSize)
            .resultList
            .also {
                println(it)
                assert(it.size == pageSize)
            }
    }
}
