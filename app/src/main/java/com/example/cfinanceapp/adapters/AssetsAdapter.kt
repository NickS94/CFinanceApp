package com.example.cfinanceapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.databinding.MarketItemBinding
import com.example.cfinanceapp.databinding.UsdAssetItemBinding
import com.example.cfinanceapp.tools.AssetType
import com.example.cfinanceapp.tools.ViewModel

class AssetsAdapter(
    private var assetsData: MutableList<Asset> = mutableListOf(),
    val viewModel: ViewModel,
    val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<Asset>) {
        assetsData = list
        notifyDataSetChanged()
    }

    companion object {
        private const val VIEW_TYPE_USD_ASSET = 1
        private const val VIEW_TYPE_CRYPTO_ASSET = 2
    }

    inner class FiatAssetsViewHolder(val binding: UsdAssetItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class CryptoAssetsViewHolder(val binding: MarketItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun getItemViewType(position: Int): Int {
        val asset = assetsData[position]

        return when (AssetType.fromAssetType(asset.fiat ?: "USD")) {
            AssetType.ASSET_TYPE_USD -> VIEW_TYPE_USD_ASSET
            AssetType.ASSET_TYPE_CRYPTO -> VIEW_TYPE_CRYPTO_ASSET

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            VIEW_TYPE_USD_ASSET -> {
                val binding =
                    UsdAssetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                FiatAssetsViewHolder(binding)
            }

            VIEW_TYPE_CRYPTO_ASSET -> {
                val binding =
                    MarketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CryptoAssetsViewHolder(binding)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return assetsData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val asset = assetsData[position]

        when (getItemViewType(position)) {
            VIEW_TYPE_CRYPTO_ASSET -> {
                val cryptoAsset = holder as CryptoAssetsViewHolder
                cryptoAsset.binding.ivLogoMarketItem.load(viewModel.getCoinLogo(asset.cryptoCurrency!!.id.toString()))
                cryptoAsset.binding.tvCoinName.text = asset.cryptoCurrency!!.name
                cryptoAsset.binding.tvCurrentPriceMarket.text = asset.amount.toString()
                cryptoAsset.binding.tvCoinSymbol.text = asset.cryptoCurrency!!.symbol
                cryptoAsset.binding.tvChangePercentageMarket.text =
                    viewModel.formatDecimalsAmount(viewModel.actualCoinPriceUpdater(asset) * asset.amount)


                when {
                    viewModel.profitOrLossInAsset(asset) > 0 -> cryptoAsset.binding.tvProfitOrLoss.setTextColor(
                        context.getColor(R.color.green)
                    )

                    viewModel.profitOrLossInAsset(asset) < 0 -> cryptoAsset.binding.tvProfitOrLoss.setTextColor(
                        context.getColor(R.color.red)
                    )

                    else -> cryptoAsset.binding.tvProfitOrLoss.setTextColor(context.getColor(R.color.white))
                }
                cryptoAsset.binding.tvProfitOrLoss.text =
                    "$${
                        String.format(
                            "%.2f",
                            viewModel.profitOrLossInAsset(asset) * asset.amount
                        )
                    } P/L"


                holder.itemView.setOnClickListener {
                    viewModel.getCurrentCoin(viewModel.actualCoinFinder(asset)!!)
                    it.findNavController().navigate(R.id.detailsFragment)
                }
            }

            VIEW_TYPE_USD_ASSET -> {
                val usdAsset = holder as FiatAssetsViewHolder
                usdAsset.binding.tvUsdAssetSymbol.text = "USD"
                usdAsset.binding.tvUsdAssetAmount.text =
                    "${String.format("%.2f", asset.amount)}$"
            }

        }
    }
}