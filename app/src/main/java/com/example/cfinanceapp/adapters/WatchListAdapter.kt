package com.example.cfinanceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.tools.ViewModel
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.databinding.WatchlistItemBinding
import com.example.cfinanceapp.ui.HomeFragmentDirections

class WatchListAdapter(
    private var dataSet: List<CryptoCurrency> = emptyList(),
    val viewModel: ViewModel
) : RecyclerView.Adapter<WatchListAdapter.ItemViewHolder>() {



    inner class ItemViewHolder(val binding: WatchlistItemBinding) :
        RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            WatchlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val coin = dataSet[position]

        holder.binding.ivChart.load(viewModel.getChartEffect(coin.id.toString()))
        holder.binding.ivCoinLogoWatchlist.load(viewModel.getCoinLogo(coin.id.toString()))

        holder.binding.tvCurrentPrice.text = "$${String.format("%.2f", coin.quote.usdData.price)}"

        holder.binding.tv24hChangePrice.text = "${String.format("%.2f", coin.quote.usdData.percentChange24h)}%"

        when {
            coin.quote.usdData.percentChange24h > 0 -> holder.binding.tv24hChangePrice.setBackgroundResource(
                R.drawable.rounded_percentage_up
            )

            coin.quote.usdData.percentChange24h < 0 -> holder.binding.tv24hChangePrice.setBackgroundResource(
                R.drawable.rounded_percentage_down
            )
        }

        holder.binding.tvCoinNameAndSymbol.text = "${coin.name}\n${coin.symbol}"


        holder.itemView.setOnClickListener {
            viewModel.setCurrentCoin(coin)
            it.findNavController().navigate(R.id.detailsFragment)
        }


    }


}