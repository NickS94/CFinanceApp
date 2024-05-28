package com.example.cfinanceapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cfinanceapp.BuildConfig
import com.example.cfinanceapp.data.api.CoinMarketCapAPI
import com.example.cfinanceapp.data.local.DatabaseInstance
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.Favorite
import com.example.cfinanceapp.data.models.ResponseAPI
import com.example.cfinanceapp.data.models.Transaction
import com.example.cfinanceapp.data.models.Wallet


var TAG = "Repository"

class Repository(
    private val api: CoinMarketCapAPI,
    private val databaseInstance: DatabaseInstance
) {


    private val key = BuildConfig.apiKey

    private val allAccounts = databaseInstance.dao.getAllAccounts()

    private val allFavorites = databaseInstance.dao.getAllFavorites()


    private val _favorites = allFavorites
    val favorites: LiveData<MutableList<Favorite>> = _favorites

    private val _accounts = allAccounts
    val accounts: LiveData<List<Account>> = _accounts

    private val _coinsList = MutableLiveData<ResponseAPI>()
    val coinsList: LiveData<ResponseAPI> = _coinsList


    suspend fun loadCryptoCurrencyList() {
        try {
            val result = api.retrofitService.getCryptoList(key = key)
            Log.d(TAG, "Crypto List :$result")
            _coinsList.value = result
        } catch (e: Exception) {
            Log.e(TAG, "Failure due to unexpected : ${e.message}")
        }
    }


    fun getAllWallets() {
        try {
            databaseInstance.dao.getAllWallets()
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }

    }


    fun getAllAccounts() {
        try {
            databaseInstance.dao.getAllAccounts()
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD :${e.message}")
        }
    }


    fun getAllAssets() {
        try {
            databaseInstance.dao.getAllAssets()
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD :${e.message}")
        }
    }

    fun getAllTransactions() {
        try {
            databaseInstance.dao.getAllTransactions()
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD :${e.message}")
        }
    }

    fun getAllFavorites() {
        try {
            databaseInstance.dao.getAllFavorites()
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD :${e.message}")
        }
    }


    suspend fun insertAccount(account: Account) {
        try {
            databaseInstance.dao.insertAccount(account)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD :${e.message}")
        }
    }

    suspend fun insertWallet(wallet: Wallet) {

        try {
            databaseInstance.dao.insertWallet(wallet)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }

    }

    suspend fun insertAssets(asset: Asset) {
        try {
            databaseInstance.dao.insertAsset(asset)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }

    suspend fun insertTransactions(transaction: Transaction) {
        try {
            databaseInstance.dao.insertTransaction(transaction)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }

    suspend fun insertFavorite(favorite: Favorite) {
        try {
            databaseInstance.dao.insertFavorite(favorite)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }

    suspend fun updateAssets(asset: Asset) {
        try {
            databaseInstance.dao.updateAsset(asset)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }

    suspend fun updateAccount(account: Account){
        try {
            databaseInstance.dao.updateAccount(account)
        }catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }

    suspend fun getAccountByEmail(email: String): Account {
        try {
            return databaseInstance.dao.getAccountByEmail(email)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }

    }


    suspend fun getWalletById(accountId: Long): Wallet {
        try {
            return databaseInstance.dao.getWalletById(accountId)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }

    }


    suspend fun getAssetsByWalletId(walletId: Long): MutableList<Asset> {
        try {
            return databaseInstance.dao.getAssetsById(walletId)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }
    }

    suspend fun getTransactionsByWalletId(walletId: Long): List<Transaction> {
        try {
            return databaseInstance.dao.getTransactionsByWalletId(walletId)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }
    }

    suspend fun getFavoritesByAccountId(accountId: Long): MutableList<Favorite> {
        try {
            return databaseInstance.dao.getFavoritesByAccountId(accountId)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }
    }

    suspend fun removeFromFavorite(favorite: Favorite) {
        try {
            databaseInstance.dao.removeFavorite(favorite)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }

    suspend fun removeAsset(asset: Asset) {
        try {
            databaseInstance.dao.removeAsset(asset)
        } catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
        }
    }


}