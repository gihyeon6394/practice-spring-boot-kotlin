package com.practice.kopring.jdbc

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.annotation.Rollback
import java.time.LocalDateTime

/**
 * @author gihyeon-kim
 */
@SpringBootTest
class BatchUpdateTest @Autowired constructor(
    private val jdbcTemplate: JdbcTemplate,
) {

    @Test
    @DisplayName("jdbc template Batch update 바인드변수 이스케이핑 테스트")
    @Rollback(false)
    fun batch_update_test() {
        val chunkSize = 2000
        val now = LocalDateTime.now().toString()

        // given
        val sql = "insert into member (name, ymdt_cre) values (?, ?)"
        val args = listOf(
            arrayOf("sims", now), //       sims
            arrayOf("sim\'s", now), //     sim's
            arrayOf("sim's", now), //      sim's
            arrayOf("sim\"s", now), //     sim"s
            arrayOf("sim`s", now), //      sim`s
            arrayOf("sim\\s", now), //     sim\s
        )

        // when
        val result = jdbcTemplate.batchUpdate(sql, args, chunkSize) { ps, arg ->
            ps.setString(1, arg[0])
            ps.setString(2, arg[1])
        }.first()

        // then
        assert(result.size == args.size)
    }
}
