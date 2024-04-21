package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cfinanceapp.tools.CryptocurrencyConverter
import com.example.cfinanceapp.tools.WalletConverter

@Entity
@TypeConverters(WalletConverter::class)
data class Account(
    @PrimaryKey(autoGenerate = true)
    val accountId: Long = 0,
    @ColumnInfo(name = "email")
    val email: String,
    @ColumnInfo(name = "password")
    val password: String
)