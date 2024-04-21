package com.example.cfinanceapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.Transactions
import com.example.cfinanceapp.data.models.Wallet
import com.example.cfinanceapp.tools.CryptocurrencyConverter
import com.example.cfinanceapp.tools.QuoteConverter
import com.example.cfinanceapp.tools.USDataConverter


@Database(entities = [Account::class, Wallet::class,Asset::class,Transactions::class], version = 1)
@TypeConverters(CryptocurrencyConverter::class, QuoteConverter::class, USDataConverter::class)
abstract class DatabaseInstance : RoomDatabase() {
    abstract val dao: ProfileDao


    companion object {
        private lateinit var INSTANCE: DatabaseInstance
        fun getDatabase(context: Context): DatabaseInstance {
            synchronized(DatabaseInstance::class.java) {
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseInstance::class.java,
                        "Profile_database"
                    ).build()

                }
                return INSTANCE
            }

        }
    }
}






