package com.example.cfinanceapp.data.API

import androidx.lifecycle.LiveData
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.data.models.ResponceAPI
import com.example.cfinanceapp.data.models.ResponceAPILogo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

const val BASE_URL = "https://pro-api.coinmarketcap.com"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()


interface CMCServiceAPI{

    @GET("/v1/cryptocurrency/listings/latest?convert=USD")
    suspend fun getCryptoList(@Header("CMC_PRO_API_KEY")key:String):List<ResponceAPI>

    @GET("/v2/cryptocurrency/info")
    suspend fun getCryptoLogo(@Header("CMC_PRO_API_KEY")key:String,@Query("symbol")symbol:String):List<ResponceAPILogo>
}

object CoinMarketCapAPI{
    val retrofitService: CMCServiceAPI by lazy { retrofit.create(CMCServiceAPI::class.java) }
}