package com.example.cfinanceapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.Favorite
import com.example.cfinanceapp.data.models.Transaction
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


    @Insert
    suspend fun insertAsset(asset: Asset)

    @Update
    suspend fun updateAsset(asset: Asset)

    @Delete
    suspend fun removeAsset (asset: Asset)


    @Query("SELECT * FROM ASSETS WHERE walletId = :walletId")
    suspend fun getAssetsById(walletId: Long):MutableList<Asset>


    @Query("SELECT * FROM TRANSACTIONS")
    fun getAllTransactions():LiveData<List<Transaction>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: Transaction)


    @Query("SELECT * FROM Transactions WHERE walletId = :walletId")
    suspend fun getTransactionsByWalletId(walletId:Long):MutableList<Transaction>

    @Query("SELECT * FROM FAVORITE")
    fun getAllFavorites():LiveData<MutableList<Favorite>>

    @Insert
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun removeFavorite(favorite: Favorite)

    @Query("SELECT * FROM FAVORITE WHERE accountId = :accountId")
    suspend fun  getFavoritesByAccountId(accountId: Long):MutableList<Favorite>


}