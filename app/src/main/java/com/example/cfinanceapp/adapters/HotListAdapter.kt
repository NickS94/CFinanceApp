package com.example.cfinanceapp.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.tools.ViewModel
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.databinding.MarketItemBinding
import com.example.cfinanceapp.ui.HomeFragmentDirections

class HotListAdapter(
    private val dataList:List<CryptoCurrency>,
    private val viewModel: ViewModel
): RecyclerView.Adapter<HotListAdapter.ItemViewHolder>() {


    inner class ItemViewHolder(val binding :MarketItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = MarketItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val coin = dataList[position]


        holder.binding.ivLogoMarketItem.load(viewModel.getCoinLogo(coin.id.toString()))
        holder.binding.tvCoinSymbol.text = coin.symbol
        holder.binding.tvCoinName.text = coin.name


        holder.binding.ivArrow.visibility = View.GONE

        holder.binding.tvCurrentPriceMarket.text = "$${String.format("%.2f",coin.quote.usdData.price)}"


        val volume24h = coin.quote.usdData.volume24h
        val formattedVolume = if (volume24h >= 1_000_000_000) {
            String.format("%.2f Bil", volume24h / 1_000_000_000)
        } else {
            String.format("%.2f Mil.", volume24h / 1_000_000)
        }
        holder.binding.tvChangePercentageMarket.text = "Volume 24H: $formattedVolume"

        holder.itemView.setOnClickListener {
            viewModel.getCurrentCrypto(position)
            it.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailsFragment(position,coin))
        }




    }


}