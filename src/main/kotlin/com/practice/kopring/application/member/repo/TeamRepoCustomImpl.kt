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
    override fun findAllTeam(pageSize: Long): List<Team> {
        return from(team)
            .leftJoin(team.members).distinct().fetchJoin()
            .offset(0)
            .limit(pageSize)
            .fetch()
    }

    override fun findAllTeamSafe(pageSize: Long): List<Team> {
        return from(team)
            .offset(0)
            .limit(pageSize)
            .fetch()
    }
}
