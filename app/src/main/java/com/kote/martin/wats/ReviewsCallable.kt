package com.kote.martin.wats

import com.kote.martin.wats.model.Review
import com.kote.martin.wats.web.WatsApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class ReviewsCallable(val apiBasePath: String) : Callable<List<Review>> {

    override fun call(): List<Review>? {
        try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)
            val call = api.getReviewsForLocation(1L)
            val reviews: List<Review>? = call.execute().body()?.content
            return reviews
        } catch (e: Exception) {
            e.printStackTrace()
            return null;
        }
    }
}