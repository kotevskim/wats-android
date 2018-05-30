package com.kote.martin.wats.model

import java.time.LocalDateTime

data class Review(val id: Long,
                  val description: String,
                  val datePublished: String,
                  val user: User
//                  val location: Location
)