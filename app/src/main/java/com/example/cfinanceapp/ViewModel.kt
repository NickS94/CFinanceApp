package com.example.cfinanceapp


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.Repository
import com.example.cfinanceapp.data.models.CryptoCurrency
import kotlinx.coroutines.launch

class ViewModel(application: Application) : AndroidViewModel(application) {




    private val repository = Repository(CoinMarketCapAPI)


    var cryptoList = repository.coinsList





    fun loadCrypto() {
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()

        }

    }


    fun getCoinLogo(id: String): String {
        return "https://s2.coinmarketcap.com/static/img/coins/64x64/$id.png"
    }

    fun search(query: String): MutableList<CryptoCurrency> {
        val searchList = mutableListOf<CryptoCurrency>()
        for (coin in cryptoList.value!!.data) {
            if (coin.name.lowercase().contains(query) || coin.symbol.lowercase().contains(query))
                searchList.add(coin)
        }
        return searchList
    }

    fun filterGainers(input: String): List<CryptoCurrency> {
        val gainers = cryptoList.value!!.data.filter { it.quote.usdData.percentChange24h > 0 }
        val losers = cryptoList.value!!.data.filter { it.quote.usdData.percentChange24h < 0 }

        return when (input) {
            "Gainers" -> gainers.sortedByDescending { it.quote.usdData.percentChange24h }
            "Losers" -> losers.sortedBy { it.quote.usdData.percentChange24h }
            "All" -> cryptoList.value!!.data
            else -> cryptoList.value!!.data
        }
    }

    fun getId(): String {
        loadCrypto()
        return cryptoList.value!!.data.forEach {
            it.id
        }.toString()

    }


}