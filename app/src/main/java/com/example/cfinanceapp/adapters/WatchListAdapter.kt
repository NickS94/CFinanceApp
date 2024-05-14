package com.example.cfinanceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.tools.ViewModel
import com.example.cfinanceapp.data.models.Favorite
import com.example.cfinanceapp.databinding.WatchlistItemBinding

class WatchListAdapter(
    private var dataSet: MutableList<Favorite> = mutableListOf(),
    val viewModel: ViewModel
) : RecyclerView.Adapter<WatchListAdapter.ItemViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: MutableList<Favorite>) {
        dataSet = list
        notifyDataSetChanged()
    }


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
        val favorite = dataSet[position]

        holder.binding.ivChart.load(viewModel.getChartEffect(favorite.favoriteCoin?.id.toString()))
        holder.binding.ivCoinLogoWatchlist.load(viewModel.getCoinLogo(favorite.favoriteCoin?.id.toString()))

        holder.binding.tvCurrentPrice.text = "$${String.format("%.2f", favorite.favoriteCoin?.quote?.usdData?.price)}"

        holder.binding.tv24hChangePrice.text = "${String.format("%.2f",favorite.favoriteCoin?.quote?.usdData?.percentChange24h)}%"

        when {
            favorite.favoriteCoin?.quote?.usdData?.percentChange24h!! > 0 -> holder.binding.tv24hChangePrice.setBackgroundResource(
                R.drawable.rounded_percentage_up
            )

            favorite.favoriteCoin.quote.usdData.percentChange24h < 0 -> holder.binding.tv24hChangePrice.setBackgroundResource(
                R.drawable.rounded_percentage_down
            )
        }

        holder.binding.tvCoinNameAndSymbol.text = "${favorite.favoriteCoin?.name}\n${favorite.favoriteCoin?.symbol}"


        holder.itemView.setOnClickListener {
            viewModel.getCurrentCoin(favorite.favoriteCoin!!)
            it.findNavController().navigate(R.id.detailsFragment)
        }


    }


}