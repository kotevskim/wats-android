package com.kote.martin.wats.model

data class ForumAnswer(val id: Long,
                       val description: String,
                       val datePublished: String,
                       val user: User
) : Item