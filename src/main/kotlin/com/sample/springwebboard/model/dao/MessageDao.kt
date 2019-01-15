package com.sample.springwebboard.model.dao

import com.sample.springwebboard.model.entity.MessageEntity
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class MessageDao(private val con: Connection) {

    fun getNextId(): Int {

        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
            val sqlBuilder: StringBuilder = StringBuilder()
            sqlBuilder.append("SELECT              ")
            sqlBuilder.append("  MAX(id) as max_id ")
            sqlBuilder.append("FROM                ")
            sqlBuilder.append("  message           ")

            ps = con.prepareStatement(sqlBuilder.toString())
            rs = ps.executeQuery()

            val nextId: Int =
                    if (rs.next()) rs.getInt("max_id") + 1
                    else 1

            return nextId
        } finally {
            ps!!.close()
            rs!!.close()
        }
    }

    fun selectMessage(): List<MessageEntity> {

        val list: MutableList<MessageEntity> = mutableListOf()
        var ps: PreparedStatement? = null
        var rs: ResultSet? = null

        try {
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

            while (rs.next()) {
                val entity: MessageEntity = MessageEntity(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("text"),
                        rs.getTimestamp("created_at")
                )
                list.add(entity)
            }

            return list ?: emptyList<MessageEntity>()

        } finally {
            ps!!.close()
            rs!!.close()
        }
    }

    fun insertMessage(entity: MessageEntity): Boolean {

        var ps: PreparedStatement? = null

        try {
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

            val result: Int = ps.executeUpdate()
            return (result == 1)

        } finally {
            ps!!.close()
        }
    }
}