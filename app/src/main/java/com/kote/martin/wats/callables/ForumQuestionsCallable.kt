package com.kote.martin.wats.callables

import com.kote.martin.wats.model.ForumQuestion
import com.kote.martin.wats.model.Page
import com.kote.martin.wats.web.WatsApi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Callable

class ForumQuestionsCallable(val apiBasePath: String) : Callable<List<ForumQuestion>> {

    override fun call(): List<ForumQuestion>? {
        try {
            val retrofit = Retrofit.Builder()
                    .baseUrl(apiBasePath)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            val api = retrofit.create(WatsApi::class.java)
            val call: Call<Page<ForumQuestion>> = api.getForumQuestionsForLocation(1L)
            return call.execute()?.body()?.content
        } catch (e: Exception) {
            e.printStackTrace()
            return null;
        }
    }
}