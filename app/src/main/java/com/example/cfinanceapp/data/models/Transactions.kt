package com.example.cfinanceapp.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Wallet::class,
        parentColumns = ["walletId"],
        childColumns = ["relationsId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Transactions(
    @PrimaryKey(autoGenerate = true)
    val transactionId: Long = 0,
    val date: String,
    val transactionHash: String,
    val relationsId: Long
)