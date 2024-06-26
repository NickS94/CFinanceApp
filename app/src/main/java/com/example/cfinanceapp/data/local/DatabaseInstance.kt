package com.example.cfinanceapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.Favorite
import com.example.cfinanceapp.data.models.Transaction
import com.example.cfinanceapp.data.models.Wallet
import com.example.cfinanceapp.tools.CryptocurrencyConverter



@Database(entities = [Account::class,Wallet::class,Asset::class,Transaction::class,Favorite::class], version = 1)
@TypeConverters(CryptocurrencyConverter::class)
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






