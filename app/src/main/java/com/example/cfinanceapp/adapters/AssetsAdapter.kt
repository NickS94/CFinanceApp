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
import com.example.cfinanceapp.tools.ViewModel

class AssetsAdapter(
    private var assetsData: MutableList<Asset> = mutableListOf(),
    val viewModel: ViewModel,
    val context: Context
) : RecyclerView.Adapter<AssetsAdapter.AssetsViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<Asset>) {
        assetsData = list
        notifyDataSetChanged()
    }


    inner class AssetsViewHolder(val binding: MarketItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetsViewHolder {
        val binding = MarketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssetsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return assetsData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: AssetsViewHolder, position: Int) {
        val asset = assetsData[position]

        when {
            asset.cryptoCurrency != null -> {

                holder.binding.ivLogoMarketItem.load(viewModel.getCoinLogo(asset.cryptoCurrency!!.id.toString()))
                holder.binding.tvCoinName.text = asset.cryptoCurrency!!.name
                holder.binding.tvCurrentPriceMarket.text = asset.amount.toString()
                holder.binding.tvCoinSymbol.text = asset.cryptoCurrency!!.symbol
                holder.binding.tvChangePercentageMarket.text =
                    "$${
                        String.format(
                            "%.2f",
                            viewModel.actualCoinPriceUpdater(asset) * asset.amount
                        )
                    }"
                when {
                    viewModel.profitOrLossInAsset(asset) > 0 -> holder.binding.tvProfitOrLoss.setTextColor(
                        context.getColor(R.color.green)
                    )

                    viewModel.profitOrLossInAsset(asset) < 0 -> holder.binding.tvProfitOrLoss.setTextColor(
                        context.getColor(R.color.red)
                    )

                    else -> holder.binding.tvProfitOrLoss.setTextColor(context.getColor(R.color.white))
                }
                holder.binding.tvProfitOrLoss.text =
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

            asset.fiat == "USD" -> {
                holder.binding.tvCoinName.text = "USD"
                holder.binding.tvChangePercentageMarket.text =
                    "${String.format("%.2f", asset.amount)}$"
                holder.binding.tvCurrentPriceMarket.text = "Amount"
                holder.binding.tvCoinSymbol.text = "Fiat Currency"
                holder.binding.ivLogoMarketItem.setImageResource(R.drawable.dollar_usd_64)
            }

        }
    }
}