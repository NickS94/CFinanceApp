package com.example.cfinanceapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.models.ResponseAPI
import com.example.cfinanceapp.data.models.ResponseAPILogo

var TAG = "Repository"
class Repository (private val api: CoinMarketCapAPI){

    private val key = com.example.cfinanceapp.BuildConfig.apiKey


    private val _coinsList = MutableLiveData<ResponseAPI>()
    private val _logoList = MutableLiveData<ResponseAPILogo>()

    val coinsList : LiveData<ResponseAPI>
        get() = _coinsList

    val logoList : LiveData<ResponseAPILogo>
        get() = _logoList





    suspend fun loadCryptoCurrencyList(){
        try {
            val result = api.retrofitService.getCryptoList(key = key)
            Log.d(TAG,"Crypto List :$result")
            _coinsList.value = result
        }catch (e:Exception){
            Log.e(TAG,"Failure due to unexpected : ${e.message}")
        }
    }


    suspend fun loadCryptoCurrencyLogoList(symbol: String){
//        try {
//            val result = api.retrofitService.getCryptoLogo(symbol,key)
//            Log.d(TAG,"Crypto Logo List :$result")
//            _logoList.value = result
//        }catch (e:Exception){
//            Log.e(TAG,"Failure due to unexpected : $e")
//        }
    }


}