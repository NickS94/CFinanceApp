package com.example.cfinanceapp.data.API

import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.data.models.ResponseAPI
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

const val BASE_URL = "https://pro-api.coinmarketcap.com"

private val logger:HttpLoggingInterceptor =HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.HEADERS)

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(logger)
    .build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .client(httpClient)
    .build()


interface CMCServiceAPI{

    @GET("/v1/cryptocurrency/listings/latest?limit=50&convert=USD")
    suspend fun getCryptoList(@Header("Accept")accept : String = "application/json",
                              @Header("X-CMC_PRO_API_KEY")key:String):ResponseAPI

}

object CoinMarketCapAPI{
    val retrofitService: CMCServiceAPI by lazy { retrofit.create(CMCServiceAPI::class.java) }
}