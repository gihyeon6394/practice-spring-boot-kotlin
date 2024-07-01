package com.practice.kopring.application.member

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "team")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column
    val name: String,
    @Column
    val ymdtCre: LocalDateTime = LocalDateTime.now(),
    @Column
    val ymdtUpdt: LocalDateTime? = null

) {
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var members: List<Member> = emptyList()
}
