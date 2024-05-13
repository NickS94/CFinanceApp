package com.example.cfinanceapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Favorite")
data class Favorite(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    @ColumnInfo(name = "Watchlist")
    val favoriteCoin : CryptoCurrency?,
    @ColumnInfo(name = "Status")
    val isFavorite : Boolean?,

    val accountId:Long
)
