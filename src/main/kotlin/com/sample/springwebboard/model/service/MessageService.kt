package com.sample.springwebboard.model.service

import com.sample.springwebboard.model.dao.MessageDao
import com.sample.springwebboard.model.entity.MessageEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.sql.Timestamp

@Service
class MessageService {

    @Autowired
    lateinit var messageDao: MessageDao

    fun getMessage(): List<MessageEntity> {

        return messageDao.selectMessage()
    }

    @Transactional
    fun postMessage(name: String, text: String) {

        val message = MessageEntity(
                messageDao.getNextId(),
                name,
                text,
                Timestamp(System.currentTimeMillis())
        )

        messageDao.insertMessage(message)
    }
}