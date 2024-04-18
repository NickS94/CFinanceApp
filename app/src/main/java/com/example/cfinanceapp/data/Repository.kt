package com.example.cfinanceapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.data.models.ResponseAPI

var TAG = "Repository"
class Repository (private val api: CoinMarketCapAPI){

    private val key = com.example.cfinanceapp.BuildConfig.apiKey


    private val _coinsList = MutableLiveData<ResponseAPI>()


    val coinsList : LiveData<ResponseAPI>
        get() = _coinsList




    suspend fun loadCryptoCurrencyList(){
        try {
            val result = api.retrofitService.getCryptoList(key=key)
            Log.d(TAG,"Crypto List :$result")
            _coinsList.value = result
        }catch (e:Exception){
            Log.e(TAG,"Failure due to unexpected : ${e.message}")
        }
    }





}