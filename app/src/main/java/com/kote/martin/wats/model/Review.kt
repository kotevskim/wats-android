package com.kote.martin.wats.model

import org.joda.time.DateTime

data class Review(val id: Long,
                  val description: String,
                  val datePublished: String,
                  val user: User
) : Item