package com.example.cfinanceapp.tools

import androidx.room.Embedded
import androidx.room.Relation
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Wallet

data class AccountWithWallet(

@Embedded val account: Account,
@Relation(
    parentColumn = "id",
    entityColumn = "accountId"
)
val wallet: Wallet
)
