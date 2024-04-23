package com.example.cfinanceapp.tools


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.Repository
import com.example.cfinanceapp.data.local.DatabaseInstance
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.CryptoCurrency
import kotlinx.coroutines.launch
import kotlin.random.Random

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(CoinMarketCapAPI,DatabaseInstance.getDatabase(application))


    private val _accountsWithWalletsAndTransactions = MutableLiveData<List<AccountWithWallet>>()
    val accountsWithWalletsAndTransactions: LiveData<List<AccountWithWallet>>
        get() = _accountsWithWalletsAndTransactions


    val accounts = repository.accounts

    private var _currentCrypto = MutableLiveData<CryptoCurrency>()

    private val _cryptoWatchList = MutableLiveData<MutableList<CryptoCurrency>>()

    var cryptoList = repository.coinsList

    val cryptoWatchList: LiveData<MutableList<CryptoCurrency>>
        get() = _cryptoWatchList


    init {

        loadCrypto()
        loadLocalData()
        getAllAccountsWithWalletsAndTransactions()
    }


    fun getAllAccountsWithWalletsAndTransactions() {
        _accountsWithWalletsAndTransactions.postValue(repository.getAllAccountsWithWalletsAndTransactions().value)
    }


      private fun loadLocalData(){
        viewModelScope.launch {
            repository.getAllAccounts()
        }
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()

        }

    }

    fun createNewAccount (account: Account){
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun isAccountAlreadyRegistered(email: String):Boolean{
        return accounts.value?.any { it.email == email } ?: false
    }

    fun findAccountByEmail(email: String): LiveData<Account?> {

        return repository.getAccountByEmail(email)
    }

    fun authenticateUser(email: String, password: String): LiveData<Boolean> {
        val authenticationResult = MutableLiveData<Boolean>()

        findAccountByEmail(email).observeForever { userAccount ->
            if (userAccount != null) {
                authenticationResult.value = userAccount.password == password
            } else {
                authenticationResult.value = false
            }
        }
        return authenticationResult
    }

    fun loadHotList(): List<CryptoCurrency> {
        val sortedByVolume = cryptoList.value!!.data
        return sortedByVolume.subList(0, 10).sortedByDescending { it.quote.usdData.volume24h }

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
            "Gain" -> gainers.sortedByDescending { it.quote.usdData.percentChange24h }
            "Lose" -> losers.sortedBy { it.quote.usdData.percentChange24h }
            "All" -> cryptoList.value!!.data
            else -> cryptoList.value!!.data
        }
    }

    fun addToWatchlist(coin: CryptoCurrency):MutableList<CryptoCurrency> {
        val updatedList = (_cryptoWatchList.value ?: emptyList()).toMutableList()
        updatedList.add(coin)
        _cryptoWatchList.value = updatedList
        return updatedList
    }

    fun getCurrentCrypto(position: Int) {
        _currentCrypto.value = cryptoList.value!!.data[position]
    }

    fun getChartEffect(coinId: String): String {
        return "https://s3.coinmarketcap.com/generated/sparklines/web/1d/usd/$coinId.png"
    }

    fun generateTransactionHash(): String {
        val hexChars = "0123456789abcdef"
        val sb = StringBuilder(32)

        repeat(32) {
            val randomIndex = Random.nextInt(0, hexChars.length)
            sb.append(hexChars[randomIndex])
        }

        return sb.toString()
    }

}