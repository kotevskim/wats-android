package com.kote.martin.wats.model

data class ForumQuestion(val id: Long,
                         val description: String,
                         val datePublished: String,
                         val user: User
) : Item