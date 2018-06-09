package com.kote.martin.wats.model

data class ReviewComment(val id: Long,
                         val description: String,
                         val datePublished: String,
                         val user: User
) : Item