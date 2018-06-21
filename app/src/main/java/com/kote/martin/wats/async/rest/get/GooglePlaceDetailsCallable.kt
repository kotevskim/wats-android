package com.kote.martin.wats.async.rest.get

import com.kote.martin.wats.model.GooglePlaceDetails
import com.kote.martin.wats.web.GooglePlaceAPI
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class GooglePlaceDetailsCallable(val googleApiKey: String, val gMapsPlaceId: String)
    : Callable<GooglePlaceDetails> {

    override fun call(): GooglePlaceDetails? {
        return try {
            val retrofit = Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(GooglePlaceAPI::class.java)
            val call: Call<GooglePlaceDetails>? = api.getPlaceDetails(googleApiKey, gMapsPlaceId)
            val x = call?.execute()?.body()
            x
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}