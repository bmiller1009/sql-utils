package org.bradfordmiller.sqlutils

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Types
import kotlin.test.assertFailsWith

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SqlUtilsTest {

    fun getConnection(): Connection {
        return DriverManager.getConnection("jdbc:sqlite:src/test/resources/data/real_estate.db")
    }

    @Test
    fun getMapFromRs() {

        val colNames = mutableMapOf(1 to "city", 2 to "state", 3 to "zip")
        val testMap = mutableMapOf<String, Any>("city" to "SACRAMENTO", "state" to "CA", "zip" to "95838")

        val map = getConnection().use {conn ->
            val sql = "SELECT city, state, zip FROM real_estate WHERE street = '3526 HIGH ST'"
            conn.prepareStatement(sql).use {stmt ->
                stmt.executeQuery().use {rs ->
                    SqlUtils.getMapFromRs(rs, colNames)
                }
            }
        }
        assert(testMap == map)
    }

    @Test
    fun getMapFromRsEmptyRs() {

        val colNames = mutableMapOf(1 to "city", 2 to "state", 3 to "zip")

        assertFailsWith<SQLException> {
            getConnection().use { conn ->
                val sql = "SELECT city, state, zip FROM real_estate WHERE 1 = 2"
                conn.prepareStatement(sql).use { stmt ->
                    stmt.executeQuery().use { rs ->
                        SqlUtils.getMapFromRs(rs, colNames)
                    }
                }
            }
        }
    }

    @Test
    fun getMapFromRsEmptyColumns() {

        val colNames = emptyMap<Int, String>()

        val map = getConnection().use {conn ->
            val sql = "SELECT city, state, zip FROM real_estate"
            conn.prepareStatement(sql).use { stmt ->
                stmt.executeQuery().use { rs ->
                    SqlUtils.getMapFromRs(rs, colNames)
                }
            }
        }
        assert(map.isEmpty())
    }

    @Test
    fun getColumnsFromRsQi() {

        val testMap = mutableMapOf(1 to "city", 2 to "state", 3 to "zip")

        val conn = getConnection()
        val sql = "SELECT city, state, zip FROM real_estate"
        val qi = SqlUtils.getQueryInfo(sql, conn)

        val columnMap = SqlUtils.getColumnsFromRs(qi)

        assert(testMap == columnMap)
    }

    @Test
    fun getColumnsFromRsRsmd() {

        val testMap = mutableMapOf(1 to "city", 2 to "state", 3 to "zip")

        val columnMap = getConnection().use {conn ->
            val sql = "SELECT city, state, zip FROM real_estate"
            conn.prepareStatement(sql).use {stmt ->
                stmt.executeQuery().use {rs ->
                    SqlUtils.getColumnsFromRs(rs.metaData)
                }
            }
        }
        assert(testMap == columnMap)
    }

    @Test
    fun getColumnIdxFromRs() {

        val testMap = mutableMapOf("city" to 1, "state" to 2, "zip" to 3)

        val conn = getConnection()
        val sql = "SELECT city, state, zip FROM real_estate"
        val qi = SqlUtils.getQueryInfo(sql, conn)

        val map = SqlUtils.getColumnIdxFromRs(qi)

        assert(testMap == map)
    }

    @Test
    fun generateInsert() {

        val testInsert = "INSERT INTO real_estate (city,state,zip) VALUES (?,?,?)"

        val conn = getConnection()
        val name = "real_estate"
        val sql = "SELECT city, state, zip FROM real_estate"
        val qi = SqlUtils.getQueryInfo(sql, conn)
        val vendor = "sqlite"

        val insert = SqlUtils.generateInsert(name, qi, vendor)

        assert(testInsert == insert)
    }

    @Test
    fun generateDDL() {

        val testDDL = "CREATE TABLE testTable (city TEXT,state TEXT,zip TEXT)"

        val conn = getConnection()
        val tableName = "testTable"
        val sql = "SELECT city, state, zip FROM real_estate"
        val qi = SqlUtils.getQueryInfo(sql, conn)
        val vendor = "sqlite"

        val ddl = SqlUtils.generateDDL(tableName, qi, vendor)

        assert(testDDL == ddl)
    }

    @Test
    fun executeDDL() {

        val conn = DriverManager.getConnection("jdbc:sqlite:src/test/resources/data/outputData/real_estate.db")
        SqlUtils.deleteTableIfExists(conn, "testTable")

        val dbmd = conn.metaData
        val testDDL = "CREATE TABLE testTable (city TEXT,state TEXT,zip TEXT)"

        SqlUtils.executeDDL(conn, testDDL)

        assert(SqlUtils.tableExists(dbmd, "testTable"))
    }

    @Test
    fun stringifyRow() {

        val testRow = "SACRAMENTO, CA, 95838"

        val row = getConnection().use {conn ->
            val sql = "SELECT city, state, zip FROM real_estate"
            conn.prepareStatement(sql).use {stmt ->
                stmt.executeQuery().use {rs ->
                    SqlUtils.stringifyRow(rs)
                }
            }
        }
        assert(testRow == row)
    }

    @Test
    fun stringifyRowWithColumnList() {

        val columns = mutableSetOf("city", "state")
        val testRow = "SACRAMENTO, CA"

        val row = getConnection().use {conn ->
            val sql = "SELECT city, state, zip FROM real_estate"
            conn.prepareStatement(sql).use {stmt ->
                stmt.executeQuery().use {rs ->
                    SqlUtils.stringifyRow(rs, columns)
                }
            }
        }
        assert(testRow == row)
    }

    @Test
    fun getQueryInfo() {

        val qmd =
            QueryMetadata(
            1,
            "city",
            12,
            "VARCHAR",
                2147483647,
                true
            )

        val testQueryInfo = QueryInfo(1, mutableSetOf(qmd))

        val qi = getConnection().use { conn ->
            val sql = "SELECT city FROM real_estate"
            SqlUtils.getQueryInfo(sql, conn)
        }

        assert(qi == testQueryInfo)
    }
}