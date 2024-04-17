package com.example.cfinanceapp

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.Repository
import kotlinx.coroutines.launch

class ViewModel(application: Application):AndroidViewModel(application) {

    private val repository = Repository(CoinMarketCapAPI)

    val cryptoList = repository.coinsList

    fun loadCrypto(){
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()
        }
    }



}