package com.kote.martin.wats.async.rest.get

import com.kote.martin.wats.model.Place
import com.kote.martin.wats.web.WatsApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class PlacesCallable(private val apiBasePath: String) : Callable<List<Place>> {

    override fun call(): List<Place>? {
        try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)
            val call: Call<List<Place>>? = api.getLocations()
            return call?.execute()?.body()
        } catch (e: Exception) {
            e.printStackTrace()
            return null;
        }
    }
}