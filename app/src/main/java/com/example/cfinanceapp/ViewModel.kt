package com.example.cfinanceapp

import android.app.Application
import android.content.Context


import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.Repository
import com.example.cfinanceapp.data.models.CryptoCurrency

import kotlinx.coroutines.launch

class ViewModel(application: Application):AndroidViewModel(application) {


    private val repository = Repository(CoinMarketCapAPI)

    var cryptoList = repository.coinsList



    init {
        loadCrypto()
    }

    fun loadCrypto(){
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()
        }
    }


    fun getCoinLogo(id: String): String {
        return "https://s2.coinmarketcap.com/static/img/coins/64x64/$id.png"
    }

    fun search(query: String):MutableList<CryptoCurrency> {
        val searchList = mutableListOf<CryptoCurrency>()
        for (coin in cryptoList.value!!.data){
            if(coin.name.lowercase().contains(query) || coin.symbol.lowercase().contains(query))
                searchList.add(coin)
        }
        loadCrypto()
        return searchList
    }



}