package com.example.cfinanceapp.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cfinanceapp.R
import com.example.cfinanceapp.data.models.Transaction
import com.example.cfinanceapp.databinding.TransactionsItemBinding
import com.example.cfinanceapp.tools.ViewModel
import java.time.format.DateTimeFormatter


class TransactionsAdapter(
    private var dataTransactions: List<Transaction> = listOf(),
    private val context : Context,
    private val viewModel : ViewModel
) : RecyclerView.Adapter<TransactionsAdapter.ItemViewHolder>() {


    @SuppressLint("NotifyDataSetChanged")
    fun submitListTransactions(list: List<Transaction>) {
         dataTransactions = list
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(val binding: TransactionsItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            TransactionsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataTransactions.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val transaction = dataTransactions[position]




        holder.binding.tvAmountTransaction.text = String.format("%.2f", transaction.amount)

        holder.binding.tvDateTransaction.text = transaction.date

        holder.binding.tvSymbolTransaction.text = transaction.symbol

        when (transaction.isBought){
            true -> {
                holder.binding.tvStatusTransaction.text = "Bought"
                holder.binding.tvStatusTransaction.setTextColor(context.getColor(R.color.green))
            }
            false -> {
                holder.binding.tvStatusTransaction.text = "Sold"
                holder.binding.tvStatusTransaction.setTextColor(context.getColor(R.color.red))
            }else -> {
                holder.binding.tvStatusTransaction.text = "Deposit"
            holder.binding.tvStatusTransaction.setTextColor(context.getColor(R.color.white))
            }
        }


        holder.itemView.setOnClickListener {
            viewModel.getCurrentTransaction(transaction)
            it.findNavController().navigate(R.id.transactionsDetailFragment)
        }

    }
}


