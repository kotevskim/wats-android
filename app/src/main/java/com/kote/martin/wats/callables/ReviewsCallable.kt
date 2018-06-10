package com.kote.martin.wats.callables

import com.kote.martin.wats.model.Page
import com.kote.martin.wats.model.Review
import com.kote.martin.wats.web.WatsApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class ReviewsCallable(private val apiBasePath: String, val locationId: Long) : Callable<List<Review>> {

    override fun call(): List<Review>? {
        try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)
            val call: Call<Page<Review>>? = api.getReviewsForLocation(locationId)
            return call?.execute()?.body()?.content
        } catch (e: Exception) {
            e.printStackTrace()
            return null;
        }
    }
}