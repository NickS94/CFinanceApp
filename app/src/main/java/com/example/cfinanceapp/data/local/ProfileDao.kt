package com.example.cfinanceapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Wallet


@Dao
interface ProfileDao {




    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)


    @Query("SELECT * FROM accounts")
    fun getAllAccounts(): LiveData<List<Account>>


    @Query("SELECT * FROM accounts WHERE email = :email")
    suspend fun getAccountByEmail(email: String):Account


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)


    @Query("SELECT * FROM Wallet")
    fun getAllWallets(): LiveData<List<Wallet>>


    @Query("SELECT * FROM Wallet WHERE accountId=:accountId")
    suspend fun getWalletById(accountId: Long):Wallet




}