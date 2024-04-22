package com.example.cfinanceapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Transactions
import com.example.cfinanceapp.data.models.Wallet


@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)


    @Query("SELECT * FROM Account")
    fun getAllAccounts(): LiveData<List<Account>>


    @Query("SELECT * FROM Account WHERE email = :email")
    fun getAccountByEmail(email: String): LiveData<Account?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)


    @Query("SELECT * FROM Wallet")
    fun getAllWallets(): LiveData<List<Wallet>>


    @Query("SELECT * FROM Wallet WHERE walletId = :walletId")
    fun getWalletById(walletId: Long): LiveData<Wallet?>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transactions)


    @Query("SELECT * FROM Transactions")
    fun getAllTransactions(): LiveData<List<Transactions>>


    @Query("SELECT * FROM Transactions WHERE relationsId = :walletId")
    fun getTransactionsByWalletId(walletId: Long): LiveData<List<Transactions>>


}