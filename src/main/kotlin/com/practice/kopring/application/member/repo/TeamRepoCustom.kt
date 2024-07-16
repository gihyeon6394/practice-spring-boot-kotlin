package com.practice.kopring.application.member.repo

import com.practice.kopring.application.member.Team

interface TeamRepoCustom {

    fun findAllTeam(): List<Team>
}
