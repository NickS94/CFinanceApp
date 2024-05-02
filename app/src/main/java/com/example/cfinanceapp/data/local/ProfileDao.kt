package com.example.cfinanceapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.Wallet


@Dao
interface ProfileDao {

    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): LiveData<List<Account>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)


    @Query("SELECT * FROM accounts WHERE email = :email")
    suspend fun getAccountByEmail(email: String):Account


    @Query("SELECT * FROM Wallet")
    fun getAllWallets(): LiveData<List<Wallet>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)


    @Query("SELECT * FROM Wallet WHERE accountId=:accountId")
    suspend fun getWalletById(accountId: Long):Wallet


    @Query("SELECT * FROM Assets")
    fun getAllAssets():LiveData<MutableList<Asset>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateAssets(asset: Asset)


    @Query("SELECT * FROM assets WHERE walletId = :walletId")
    suspend fun getAssetById(walletId: Long):Asset

    @Query("SELECT * FROM ASSETS WHERE walletId = :walletId")
    suspend fun getAssetsById(walletId: Long):MutableList<Asset>


}