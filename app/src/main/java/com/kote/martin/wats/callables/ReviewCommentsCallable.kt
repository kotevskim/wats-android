package com.kote.martin.wats.callables

import com.kote.martin.wats.model.Page
import com.kote.martin.wats.model.ReviewComment
import com.kote.martin.wats.web.WatsApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class ReviewCommentsCallable(private val apiBasePath: String, val locationId: Long, private val reviewId: Long) : Callable<List<ReviewComment>> {

    override fun call(): List<ReviewComment>? {
        return try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)
            val call: Call<Page<ReviewComment>>? = api.getReviewComments(locationId, reviewId)
            call?.execute()?.body()?.content
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}