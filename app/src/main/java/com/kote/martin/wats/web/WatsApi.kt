package com.kote.martin.wats.web

import com.kote.martin.wats.model.*
import com.kote.martin.wats.web.request.AnswerRequest
import com.kote.martin.wats.web.request.QuestionRequest
import com.kote.martin.wats.web.request.ReviewRequest
import retrofit2.Call
import retrofit2.http.*

interface WatsApi {

    // TODO refactor, it is hardcoded
    @GET("locations/{locationId}/reviews?size=100&sort=datePublished,desc")
    fun getReviewsForLocation(@Path("locationId") id: Long): Call<Page<Review>>

    // TODO refactor, it is hardcoded
    @GET("locations/{locationId}/reviews/{reviewId}/comments?size=100&sort=datePublished,desc")
    fun getReviewComments(@Path("locationId") locationId: Long,
                          @Path("reviewId") reviewId: Long): Call<Page<ReviewComment>>

    // TODO refactor, it is hardcoded
    @GET("locations/{locationId}/forum/questions?size=100&sort=datePublished,desc")
    fun getForumQuestionsForLocation(@Path("locationId") id: Long): Call<Page<ForumQuestion>>

    // TODO refactor, it is hardcoded
    @GET("locations/{locationId}/forum/questions/{questionId}/answers?size=100&sort=datePublished,desc")
    fun getAnswersForForumQuestion(@Path("locationId") locationId: Long,
                                   @Path("questionId") questionId: Long): Call<Page<ForumAnswer>>

    @GET("all-locations")
    fun getLocations(): Call<List<Place>>

    @POST("locations/{locationId}/reviews")
    fun postReview(@Path("locationId") id: Long,
                   @Body description: String,
                   @Header("Authorization") jwt: String): Call<Review>?

    @POST("locations/{locationId}/reviews/{reviewId}/comments")
    fun postReviewComment(@Path("locationId") locationId: Long,
                          @Path("reviewId") reviewId: Long,
                          @Body description: String,
                          @Header("Authorization") jwt: String): Call<ReviewComment>?

    @POST("locations/{locationId}/forum/questions")
    fun postForumQuestion(@Path("locationId") id: Long,
                          @Body question: String,
                          @Header("Authorization") jwt: String): Call<ForumQuestion>?

    @POST("locations/{locationId}/forum/questions/{questionId}/answers")
    fun postForumAnswer(@Path("locationId") locationId: Long,
                        @Path("questionId") questionId: Long,
                        @Body answer: String,
                        @Header("Authorization") jwt: String): Call<ForumAnswer>?

    @POST("login")
    fun login(@Body credentials: LoginCredentials): Call<User>
}