package com.example.cfinanceapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.data.API.BASE_URL
import com.example.cfinanceapp.data.models.CryptoCurrency

import com.example.cfinanceapp.data.models.ResponseAPI
import com.example.cfinanceapp.databinding.MarketItemBinding
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class MarketAdapter(
    private val dataListCrypto: List<CryptoCurrency>,
    val context: Context
) : RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {


    inner class MarketViewHolder(val binding: MarketItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val binding = MarketItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MarketViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataListCrypto.size
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val coin = dataListCrypto[position]


        holder.binding.tvCoinName.text = coin.name
        holder.binding.tvCoinSymbol.text = coin.symbol
        holder.binding.tvCurrentPriceMarket.text = String.format("%.2f", coin.quote.usdData.price)
        "${String.format("%.2f", coin.quote.usdData.percentChange24h)}%".also { holder.binding.tvChangePercentageMarket.text = it }
        holder.binding.ivLogoMarketItem.load("https://s2.coinmarketcap.com/static/img/coins/64x64/" + coin.id + ".png")

        when {
            coin.quote.usdData.percentChange24h > 0 -> {
                holder.binding.ivArrow.load(R.drawable.price_up_arrow)
                holder.binding.tvChangePercentageMarket.setTextColor(context.resources.getColor(R.color.green))
            }
            coin.quote.usdData.percentChange24h < 0 -> {
                holder.binding.ivArrow.load(R.drawable.price_down_arrow)
                holder.binding.tvChangePercentageMarket.setTextColor(context.resources.getColor(R.color.red))
            }
        }




    }


}