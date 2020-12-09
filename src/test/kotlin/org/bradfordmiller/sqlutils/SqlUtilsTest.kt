package org.bradfordmiller.sqlutils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.DriverManager

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqlUtilsTest {

    @Test
    fun getMapFromRs() {

        val colNames = mutableMapOf(1 to "city", 2 to "state", 3 to "zip")
        val testMap = mutableMapOf<String, Any>("city" to "SACRAMENTO", "state" to "CA", "zip" to "95838")

        val map = DriverManager.getConnection("jdbc:sqlite:src/test/resources/data/real_estate.db").use {conn ->
            val sql = "SELECT city, state, zip FROM real_estate WHERE street = '3526 HIGH ST'"
            conn.prepareStatement(sql).use {stmt ->
                stmt.executeQuery().use {rs ->
                    SqlUtils.getMapFromRs(rs, colNames)
                }
            }
        }
        assert(testMap == map)
    }
}