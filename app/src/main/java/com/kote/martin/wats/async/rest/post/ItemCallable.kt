package com.kote.martin.wats.async.rest.post

import com.kote.martin.wats.model.*
import com.kote.martin.wats.web.WatsApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class ItemCallable(private val apiBasePath: String,
                   private val jwt: String,
                   private val locationId: Long,
                   private val parentId: Long?,
                   private val desc: String,
                   private val clazz: Class<out Item>)
    : Callable<Item> {

    override fun call(): Item? {
        return try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)

            var call: Call<out Item>? = null
            if (clazz == Review::class.java) {
//                val reviewRequest = ReviewRequest(desc)
                call = api.postReview(locationId, desc, jwt)
            }
            if (clazz == ReviewComment::class.java) {
//                val reviewCommentRequest = ReviewRequest(desc)
                call= api.postReviewComment(locationId, parentId!!, desc, jwt)
            }
            if (clazz == ForumQuestion::class.java) {
//                val questionRequest = QuestionRequest(desc)
                call = api.postForumQuestion(locationId, desc, jwt)
            }
            if (clazz == ForumAnswer::class.java) {
//                val answerRequest = AnswerRequest(desc)
                call = api.postForumAnswer(locationId, parentId!!, desc, jwt)
            }
            return call?.execute()?.body()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}