package com.practice.kopring.application.member.repo

import com.practice.kopring.application.member.QMember
import com.practice.kopring.application.member.QTeam
import com.practice.kopring.application.member.Team
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

/**
 * @author gihyeon-kim
 */
class TeamRepoCustomImpl : TeamRepoCustom, QuerydslRepositorySupport(Team::class.java) {
    private val team = QTeam.team
    private val member = QMember.member
    override fun findAllTeam(): List<Team> {
        return from(team)
            .leftJoin(team.members).distinct().fetchJoin()
            .limit(1)
            .fetch()
    }
}
