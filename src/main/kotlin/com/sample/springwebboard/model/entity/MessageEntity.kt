package com.sample.springwebboard.model.entity

import java.sql.Timestamp

data class MessageEntity(
        val id: Int,
        val name: String,
        val text: String,
        val created_at: Timestamp
)