package com.kote.martin.wats.web

import com.kote.martin.wats.model.Page
import com.kote.martin.wats.model.Review
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface WatsApi {

    @GET("locations/{locationId}/reviews")
    fun getReviewsForLocation(@Path("locationId") id: Long): Call<Page<Review>>
}