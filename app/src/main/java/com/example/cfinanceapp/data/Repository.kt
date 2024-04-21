package com.example.cfinanceapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
import com.example.cfinanceapp.data.local.DatabaseInstance
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.ResponseAPI
import com.example.cfinanceapp.data.models.Transactions
import com.example.cfinanceapp.data.models.Wallet


var TAG = "Repository"
class Repository (private val api: CoinMarketCapAPI,private val databaseInstance: DatabaseInstance){

    private val key = com.example.cfinanceapp.BuildConfig.apiKey


    private val allAccounts = databaseInstance.dao.getAllAccounts()

    private val _accounts = allAccounts



    val accounts : LiveData<List<Account>>
        get() = _accounts


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


    // Insert an account
    suspend fun insertAccount(account: Account) {
        try {
            databaseInstance.dao.insertAccount(account)
        }catch (e:Exception){
            Log.d(TAG,"FAILED TO LOAD :${e.message}")
        }
    }

    // Get all accounts
    fun getAllAccounts(): LiveData<List<Account>> {
        try {
             databaseInstance.dao.getAllAccounts()
        }catch (e:Exception){
            Log.d(TAG,"FAILED TO LOAD :${e.message}")
        }
        return databaseInstance.dao.getAllAccounts()
    }

    // Get an account by email
    fun getAccountByEmail(email: String): LiveData<Account?> {
        try {
            databaseInstance.dao.getAccountByEmail(email)
        }catch (e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getAccountByEmail(email)
    }

    // Insert a wallet
    suspend fun insertWallet(wallet: Wallet) {

        try {
            databaseInstance.dao.insertWallet(wallet)
        }catch (e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }


    }

    // Get all wallets
    fun getAllWallets(): LiveData<List<Wallet>> {
        try {
            databaseInstance.dao.getAllWallets()
        }catch (e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getAllWallets()
    }




    // Get a wallet by ID
    fun getWalletById(walletId: Long): LiveData<Wallet?> {
        try{
            databaseInstance.dao.getWalletById(walletId)
        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getWalletById(walletId)
    }

    // Insert an asset
    suspend fun insertAsset(asset: Asset) {
        try{
            databaseInstance.dao.insertAsset(asset)

        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
    }

    // Get all assets
    fun getAllAssets(): LiveData<List<Asset>> {
        try{
            databaseInstance.dao.getAllAssets()
        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getAllAssets()
    }

    // Get assets by walletId
    fun getAssetsByWalletId(walletId: Long): LiveData<List<Asset>> {
        try{
            databaseInstance.dao.getAssetsByWalletId(walletId)
        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getAssetsByWalletId(walletId)
    }

    // Insert a transaction
    suspend fun insertTransaction(transaction: Transactions) {
        try{
            databaseInstance.dao.insertTransaction(transaction)
        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }

    }

    // Get all transactions
    fun getAllTransactions(): LiveData<List<Transactions>> {
        try{
            databaseInstance.dao.getAllTransactions()
        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getAllTransactions()
    }

    // Get transactions by walletId
    fun getTransactionsByWalletId(walletId: Long): LiveData<List<Transactions>> {
        try{
            databaseInstance.dao.getTransactionsByWalletId(walletId)
        }catch(e:Exception){
            Log.d(TAG,"FAILED TO LOAD : ${e.message}")
        }
        return databaseInstance.dao.getTransactionsByWalletId(walletId)
    }



}