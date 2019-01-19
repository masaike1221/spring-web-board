package com.sample.springwebboard.model.dao

import com.sample.springwebboard.model.entity.MessageEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.datasource.DataSourceUtils
import org.springframework.stereotype.Repository
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import javax.sql.DataSource

@Repository
class MessageDao() {

    @Autowired
    lateinit var dataSource: DataSource

    fun getNextId(): Int {

        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            val con = getConnection()

            val sqlBuilder = StringBuilder()
            sqlBuilder.append("SELECT              ")
            sqlBuilder.append("  MAX(id) as max_id ")
            sqlBuilder.append("FROM                ")
            sqlBuilder.append("  message           ")

            ps = con.prepareStatement(sqlBuilder.toString())
            rs = ps.executeQuery()

            return if (rs.next()) rs.getInt("max_id") + 1
                   else 1

        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException("SQL Error: Select MaxId")
        } finally {
            rs!!.close()
            ps!!.close()
        }
    }

    fun selectMessage(): List<MessageEntity> {

        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            val con = getConnection()

            val sqlBuilder: StringBuilder = StringBuilder()
            sqlBuilder.append("SELECT       ")
            sqlBuilder.append("  id,        ")
            sqlBuilder.append("  name,      ")
            sqlBuilder.append("  text,      ")
            sqlBuilder.append("  created_at ")
            sqlBuilder.append("FROM         ")
            sqlBuilder.append("  message    ")
            sqlBuilder.append("ORDER BY     ")
            sqlBuilder.append("  id         ")

            ps = con.prepareStatement(sqlBuilder.toString())
            rs = ps.executeQuery()

            val list: MutableList<MessageEntity> = mutableListOf()
            while (rs.next()) {
                val entity = MessageEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text"),
                        rs.getTimestamp("created_at")
                )
                list.add(entity)
            }

            return list ?: emptyList<MessageEntity>()

        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException("SQL ERROR: Select Message")
        } finally {
            rs!!.close()
            ps!!.close()
        }
    }

    fun insertMessage(entity: MessageEntity) {

        var ps: PreparedStatement? = null

        try {
            val con = getConnection()

            val sqlBuilder: StringBuilder = StringBuilder()
            sqlBuilder.append("INSERT INTO message ( ")
            sqlBuilder.append("  id,                 ")
            sqlBuilder.append("  name,               ")
            sqlBuilder.append("  text,               ")
            sqlBuilder.append("  created_at          ")
            sqlBuilder.append(") VALUES (            ")
            sqlBuilder.append("  ?,                  ")
            sqlBuilder.append("  ?,                  ")
            sqlBuilder.append("  ?,                  ")
            sqlBuilder.append("  ?                   ")
            sqlBuilder.append(")                     ")

            ps = con.prepareStatement(sqlBuilder.toString())
            ps.setInt(1, entity.id)
            ps.setString(2, entity.name)
            ps.setString(3, entity.text)
            ps.setTimestamp(4, entity.created_at)

            ps.executeUpdate()

        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException("SQL ERROR: Insert Message")
        } finally {
            ps!!.close()
        }
    }

    private fun getConnection(): Connection {
        return DataSourceUtils.getConnection(dataSource)
    }
}