package com.example.cfinanceapp.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Wallet")

data class Wallet(
    @PrimaryKey(autoGenerate = true)
    val id : Long = 0,
    val accountId : Long
)
