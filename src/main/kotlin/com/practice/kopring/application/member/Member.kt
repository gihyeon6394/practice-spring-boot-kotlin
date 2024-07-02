package com.practice.kopring.application.member

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "member")
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long? = null,
    @Column val name: String,
    @Column val ymdtCre: LocalDateTime = LocalDateTime.now(),
    @Column val ymdtUpdt: LocalDateTime? = null,

    ) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_team")
    var team: Team? = null
}
