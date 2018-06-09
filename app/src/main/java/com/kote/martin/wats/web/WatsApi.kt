package com.kote.martin.wats.web

import com.kote.martin.wats.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WatsApi {

    @GET("locations/{locationId}/reviews")
    fun getReviewsForLocation(@Path("locationId") id: Long): Call<Page<Review>>

    @GET("locations/{locationId}/reviews/{reviewId}/comments")
    fun getReviewComments(@Path("locationId") locationId: Long,
                          @Path("reviewId") reviewId: Long): Call<Page<ReviewComment>>

    @GET("locations/{locationId}/forum/questions")
    fun getForumQuestionsForLocation(@Path("locationId") id: Long): Call<Page<ForumQuestion>>

    @GET("locations/{locationId}/forum/questions/{questionId}/answers")
    fun getAnswersForForumQuestion(@Path("locationId") locationId: Long,
                          @Path("questionId") questionId: Long): Call<Page<ForumAnswer>>
}