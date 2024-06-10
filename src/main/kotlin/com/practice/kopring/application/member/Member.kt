package com.practice.kopring.application.member

import jakarta.persistence.*
import java.time.LocalDateTime

/*
create table kopring.member
(
id          bigint auto_increment
primary key,
name_member varchar(30)                        not null,
ymdt_cre    datetime default CURRENT_TIMESTAMP not null,
ymdt_updt   datetime                           null
)
comment '멤버';
*/


@Entity
@Table(name = "member")
data class Member(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @Column
    val nameMember: String,
    @Column
    val ymdtCre: LocalDateTime = LocalDateTime.now(),
    @Column
    val ymdtUpdt: LocalDateTime?
)
