package com.sample.springwebboard.model.service

import com.sample.springwebboard.model.dao.MessageDao
import com.sample.springwebboard.model.entity.MessageEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.sql.Connection
import java.sql.SQLException
import java.sql.Timestamp
import javax.sql.DataSource

@Service
class MessageService {

    @Autowired
    lateinit var dataSource: DataSource

    fun getMessage(): List<MessageEntity> {

        try {
            val dao: MessageDao = MessageDao(dataSource.connection)
            return dao.selectMessage()

        } catch (e: SQLException) {
            e.printStackTrace()
            throw RuntimeException("SQL: Select Error")
        }
    }

    fun postMessage(name: String, text: String) {

        val con: Connection = dataSource.connection
        con.autoCommit = false

        try {

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
            e.printStackTrace()
            con.rollback()
            throw RuntimeException("SQL: Insert Error")
        }
    }
}