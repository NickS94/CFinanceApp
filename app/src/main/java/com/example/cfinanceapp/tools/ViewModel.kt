package com.example.cfinanceapp.tools


import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.Repository
import com.example.cfinanceapp.data.local.DatabaseInstance
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.data.models.Wallet
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.launch
import kotlin.random.Random

class ViewModel(application: Application) : AndroidViewModel(application) {


    private val firebaseAuthentication = Firebase.auth

    private val repository = Repository(CoinMarketCapAPI, DatabaseInstance.getDatabase(application))


    private var _currentAccount = MutableLiveData<Account>()
    val currentAccount: LiveData<Account> = _currentAccount


    private var _currentWallet = MutableLiveData<Wallet>()
    val currentWallet: LiveData<Wallet> = _currentWallet

    private var _currentCrypto = MutableLiveData<CryptoCurrency>()
    val currentCrypto: LiveData<CryptoCurrency> = _currentCrypto


    val accounts = repository.accounts


    private val _cryptoWatchList = MutableLiveData<MutableList<CryptoCurrency>>()

    var cryptoList = repository.coinsList

    val cryptoWatchList: LiveData<MutableList<CryptoCurrency>>
        get() = _cryptoWatchList


    init {
        loadCrypto()
        loadLocalData()
    }


    private fun loadLocalData() {
        viewModelScope.launch {
            repository.getAllAccounts()
        }
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()

        }

    }

    fun getCurrentCoin(coin: CryptoCurrency) {
        _currentCrypto.value = coin
    }

    fun getCurrentAccount(account: Account) {
        _currentAccount.value = account
    }

    fun createNewAccount(account: Account) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun isAccountAlreadyRegistered(email: String): Boolean {
        return accounts.value?.any { it.email == email } ?: false
    }

    fun findWalletByUserId(accountId: Long) {
        viewModelScope.launch {
            _currentWallet.value = repository.getWalletById(accountId)
        }
    }

    fun findAccountByEmail(email: String) {
        viewModelScope.launch {
            _currentAccount.value = repository.getAccountByEmail(email)
        }
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

    fun filteredLists(input: String): List<CryptoCurrency> {
        val gainers = cryptoList.value!!.data.filter { it.quote.usdData.percentChange24h > 0 }
        val losers = cryptoList.value!!.data.filter { it.quote.usdData.percentChange24h < 0 }

        return when (input) {
            "Gain" -> gainers.sortedByDescending { it.quote.usdData.percentChange24h }
            "Lose" -> losers.sortedBy { it.quote.usdData.percentChange24h }
            "All" -> cryptoList.value!!.data
            else -> cryptoList.value!!.data
        }
    }

    fun addToWatchlist(coin: CryptoCurrency): MutableList<CryptoCurrency> {
        val updatedList = (_cryptoWatchList.value ?: emptyList()).toMutableList()
        updatedList.add(coin)
        _cryptoWatchList.value = updatedList
        return updatedList
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

    fun registration(email: String, password: String, completion: () -> Unit) {
        val account = Account(email = email)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        createNewAccount(account)
                        completion()
                    } else {
                        Log.e("FIREBASE", it.exception.toString())
                        Log.e("FIREBASE", "email: $email")
                        Log.e("FIREBASE", "password : $password")

                    }
                }
        }
    }


    fun loginAuthentication(email: String, password: String, completion: () -> Unit) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuthentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        completion()
                    } else {
                        Log.e("FIREBASE", it.exception.toString())
                        Log.e("FIREBASE", "email: $email")
                        Log.e("FIREBASE", "password : $password")

                    }
                }
        }
    }


}