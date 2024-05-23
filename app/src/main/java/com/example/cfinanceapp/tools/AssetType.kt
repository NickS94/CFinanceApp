package com.example.cfinanceapp.tools

enum class AssetType {

    ASSET_TYPE_USD,
    ASSET_TYPE_CRYPTO;

    companion object{
        fun fromAssetType(fiat:String):AssetType{
            return if (fiat == "USD"){
                ASSET_TYPE_USD
            }else{
                ASSET_TYPE_CRYPTO
            }
        }
    }

}