package com.example.cfinanceapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.Transactions
import com.example.cfinanceapp.data.models.Wallet


@Dao
interface ProfileDao {
    // Insert an account
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account)

    // Get all accounts
    @Query("SELECT * FROM Account")
    fun getAllAccounts(): LiveData<List<Account>>

    // Get an account by email
    @Query("SELECT * FROM Account WHERE email = :email")
    fun getAccountByEmail(email: String): LiveData<Account?>

    // Insert a wallet
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWallet(wallet: Wallet)

    // Get all wallets
    @Query("SELECT * FROM Wallet")
    fun getAllWallets(): LiveData<List<Wallet>>

    // Get a wallet by ID
    @Query("SELECT * FROM Wallet WHERE walletId = :walletId")
    fun getWalletById(walletId: Long): LiveData<Wallet?>

    // Insert an asset
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAsset(asset: Asset)

    // Get all assets
    @Query("SELECT * FROM Asset")
    fun getAllAssets(): LiveData<List<Asset>>

    // Get assets by accountId
    @Query("SELECT * FROM Asset WHERE walletId = :walletId")
    fun getAssetsByWalletId(walletId: Long): LiveData<List<Asset>>

    // Insert a transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transactions)

    // Get all transactions
    @Query("SELECT * FROM Transactions")
    fun getAllTransactions(): LiveData<List<Transactions>>

    // Get transactions by walletId
    @Query("SELECT * FROM Transactions WHERE relationsId = :walletId")
    fun getTransactionsByWalletId(walletId: Long): LiveData<List<Transactions>>


}