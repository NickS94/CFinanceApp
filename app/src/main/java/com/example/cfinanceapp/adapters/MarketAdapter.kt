package com.example.cfinanceapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.data.API.BASE_URL
import com.example.cfinanceapp.data.models.CryptoCurrency

import com.example.cfinanceapp.data.models.ResponseAPI
import com.example.cfinanceapp.databinding.MarketItemBinding

class MarketAdapter(
    val dataListCrypto : List<CryptoCurrency>,
    val viewModel: ViewModel
): RecyclerView.Adapter<MarketAdapter.MarketViewHolder>() {


    inner class MarketViewHolder(val binding: MarketItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarketViewHolder {
        val binding = MarketItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MarketViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataListCrypto.size
    }

    override fun onBindViewHolder(holder: MarketViewHolder, position: Int) {
        val coin = dataListCrypto[position]
//
//        holder.binding.tvCoinName.text = coin
//        holder.binding.tvCoinSymbol.text = coin



    }


}