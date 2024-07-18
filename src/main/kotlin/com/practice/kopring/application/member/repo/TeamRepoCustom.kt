package com.practice.kopring.application.member.repo

import com.practice.kopring.application.member.Team

interface TeamRepoCustom {

    fun findAllTeam(pageSize: Long): List<Team>
    fun findAllTeamSafe(pageSize: Long): List<Team>
}
