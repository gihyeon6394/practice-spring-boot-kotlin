package com.practice.kopring.application.member.repo

import com.practice.kopring.application.member.Member
import com.practice.kopring.application.member.Team
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TeamRepo : JpaRepository<Team, Long>