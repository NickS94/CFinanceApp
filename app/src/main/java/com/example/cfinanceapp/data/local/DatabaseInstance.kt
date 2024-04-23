package com.example.cfinanceapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Wallet
import com.example.cfinanceapp.tools.CryptocurrencyConverter
import com.example.cfinanceapp.tools.QuoteConverter
import com.example.cfinanceapp.tools.USDataConverter
import com.example.cfinanceapp.tools.WalletConverter


@Database(entities = [Account::class,Wallet::class], version = 1)
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
                    )
                        .build()

                }
                return INSTANCE
            }

        }
    }
}






