package com.kote.martin.wats.web

import com.kote.martin.wats.model.GooglePlaceDetails
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlaceAPI {

    @GET("place/details/json")
    fun getPlaceDetails(@Query("key") key: String,
                              @Query("placeid") placeid: String): Call<GooglePlaceDetails>
}