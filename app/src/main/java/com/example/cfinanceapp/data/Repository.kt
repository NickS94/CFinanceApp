package com.example.cfinanceapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cfinanceapp.BuildConfig
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.local.DatabaseInstance
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.ResponseAPI
import com.example.cfinanceapp.data.models.Wallet


var TAG = "Repository"

class Repository(
    private val api: CoinMarketCapAPI,
    private val databaseInstance: DatabaseInstance
) {


    private val key = BuildConfig.apiKey

    private val allAssets = databaseInstance.dao.getAllAssets()

    private val allAccounts = databaseInstance.dao.getAllAccounts()

    private val allWallets = databaseInstance.dao.getAllWallets()


    private  val _assets = allAssets
    val assets :LiveData<MutableList<Asset>> = _assets

    private val _wallets = allWallets
    val wallets: LiveData<List<Wallet>> = _wallets

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


    fun getAllAssets(){
        try {
            databaseInstance.dao.getAllAssets()
        }catch (e: Exception) {
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

    suspend fun insertAssets(asset: Asset){
        try {
            databaseInstance.dao.insertOrUpdateAssets(asset)
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

    suspend fun getAssetByWalletId(walletId:Long):Asset{
        try {
            return databaseInstance.dao.getAssetById(walletId)
        }catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }
    }

    suspend fun getAssetsByWalletId(walletId:Long):MutableList<Asset>{
        try {
            return databaseInstance.dao.getAssetsById(walletId)
        }catch (e: Exception) {
            Log.d(TAG, "FAILED TO LOAD : ${e.message}")
            throw e
        }
    }


}