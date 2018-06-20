package com.kote.martin.wats.async.rest.get

import com.kote.martin.wats.model.ForumAnswer
import com.kote.martin.wats.model.Page
import com.kote.martin.wats.web.WatsApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable


class ForumAnswersCallable(private val apiBasePath: String,
                           private val locationId: Long,
                           private val questionId: Long)
    : Callable<List<ForumAnswer>> {

    override fun call(): List<ForumAnswer>? {
        return try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)
            val call: Call<Page<ForumAnswer>>? = api.getAnswersForForumQuestion(locationId, questionId)
            call?.execute()?.body()?.content
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}