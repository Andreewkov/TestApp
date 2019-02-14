package app.andreew.vktest.api

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

interface ApiInterface {

    companion object {
        fun getApi(): ApiInterface {
            var retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://newsapi.org")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }

    @GET("/v2/everything?domains=wsj.com&apiKey=beaed7e507124c53a2b64b13ff6773ed")
    fun getNews(): Call<String>
}