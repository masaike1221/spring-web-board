package com.sample.springwebboard.service

import com.sample.springwebboard.model.dao.MessageDao
import com.sample.springwebboard.model.entity.MessageEntity
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Timestamp

@Service
class MessageService {

    fun getMessage(): List<MessageEntity> {

        var con: Connection? = null

        try {
            con = getConnection()
            val dao: MessageDao = MessageDao(con)
            return dao.selectMessage()

        } catch (e: SQLException) {
            println("log [error:getMessage]")
            e.printStackTrace()
            throw RuntimeException()
        } finally {
            close(con!!)
        }
    }

    fun postMessage(name: String, text: String) {

        var con: Connection? = null

        try {
            con = getConnection()
            con.autoCommit = false

            val dao: MessageDao = MessageDao(con)

            val message: MessageEntity = MessageEntity(
                    dao.getNextId(),
                    name,
                    text,
                    Timestamp(System.currentTimeMillis())
            )

            if (dao.insertMessage(message)) {
                con.commit()
            } else {
                con.rollback()
            }
        } catch (e: SQLException) {
            println("log [error:postMessage]")
            con!!.rollback()
            e.printStackTrace()
            throw RuntimeException()
        } finally {
            close(con!!);
        }
    }

    private fun getConnection(): Connection {

        val url:  String = "jdbc:mysql://localhost/develop?useSSL=false"
        val user: String = "root"
        val pass: String = "pass"

        Class.forName("com.mysql.jdbc.Driver")
        return DriverManager.getConnection(url, user, pass)
    }

    private fun close(connection: Connection) {
        if (!connection.isClosed) {
            connection.close()
        }
    }
}