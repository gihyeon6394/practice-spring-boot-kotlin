package com.practice.kopring.application.blog

import com.practice.kopring.application.member.Member
import com.practice.kopring.application.member.Team
import com.practice.kopring.application.member.repo.MemberRepo
import com.practice.kopring.application.member.repo.TeamRepo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime

@SpringBootTest
class HHH000104Test @Autowired constructor(
    private val teamRepo: TeamRepo,
    private val memberRepo: MemberRepo,
) {

    @BeforeEach
    fun setUp() {
        memberRepo.deleteAll()
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
        aespa.members = listOf(karina, winter)

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
        newJeans.members = listOf(minzi, haerin, hani)
        teamRepo.saveAll(listOf(aespa, newJeans))
    }

    @Test
    @DisplayName("데이터 잘 들어 갔는가?")
    fun test() {
        val members = memberRepo.findAll()
        assert(members.size == 5)
        println(members)
    }
}