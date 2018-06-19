package com.kote.martin.wats.web

import com.kote.martin.wats.model.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("all-locations")
    fun getLocations(): Call<List<Place>>

    @POST("locations/{locationId}/reviews")
    fun postReview(@Path("locationId") id: Long,
                   @Body reviewRequest: ReviewRequest) : Call<Review>

    @POST("locations/{locationId}/reviews/{reviewId}/comments")
    fun postReviewComment(@Path("locationId") locationId: Long,
                          @Path("reviewId") reviewId: Long,
                          @Body reviewRequest: ReviewRequest) : Call<ReviewComment>

    @POST("locations/{locationId}/forum/questions")
    fun postForumQuestion(@Body questionRequest: QuestionRequest,
                          @Path("locationId") id: Long): Call<ForumQuestion>

    @POST("locations/{locationId}/forum/questions/{questionId}/answers")
    fun postForumAnswer(@Path("locationId") locationId: Long,
                        @Path("questionId") questionId: Long,
                        @Body answerRequest: AnswerRequest) : Call<ForumAnswer>

    @POST("login")
    fun login(@Body credentials: LoginCredentials) : Call<User>
}

class ReviewRequest(val description: String)
class QuestionRequest(val question: String)
class AnswerRequest(val answer: String)