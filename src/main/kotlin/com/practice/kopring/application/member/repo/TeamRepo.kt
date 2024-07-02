package com.practice.kopring.application.member.repo

import com.practice.kopring.application.member.Team
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TeamRepo : JpaRepository<Team, Long>, TeamRepoCustom {

    @Query("SELECT DISTINCT t FROM Team t join fetch t.members")
    fun findTopN(pageable: Pageable): List<Team>
}
