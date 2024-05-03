package com.example.cfinanceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.data.models.Asset

import com.example.cfinanceapp.databinding.MarketItemBinding
import com.example.cfinanceapp.tools.ViewModel

class AssetsAdapter(
    private var assetsData: MutableList<Asset> = mutableListOf(),
    val viewModel: ViewModel
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

        holder.binding.ivLogoMarketItem.load(viewModel.getCoinLogo(asset.cryptoCurrency!!.id.toString()))

        holder.binding.tvCoinName.text = asset.cryptoCurrency.name

        holder.binding.tvCurrentPriceMarket.text =
            "$${String.format("%.2f", asset.cryptoCurrency.quote.usdData.price * asset.amount)}"

        holder.binding.tvCoinSymbol.text = asset.cryptoCurrency.symbol

        holder.binding.tvChangePercentageMarket.text = asset.amount.toString()


    }


}