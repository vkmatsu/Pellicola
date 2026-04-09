package com.pellicola.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OmdbClient {

    // ⚠️ IMPORTANTE: Cadastre-se em https://www.omdbapi.com/apikey.aspx
    // e substitua esta chave pela sua chave gratuita!
    const val API_KEY = "dc3b68d8"

    private const val BASE_URL = "https://www.omdbapi.com/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: OmdbApiService = retrofit.create(OmdbApiService::class.java)
}
