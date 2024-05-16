package com.example.cfinanceapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.databinding.FragmentTransactionsDetailBinding
import com.example.cfinanceapp.tools.ViewModel


class TransactionsDetailFragment : Fragment() {

    private lateinit var viewBinding: FragmentTransactionsDetailBinding
    private val viewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTransactionsDetailBinding.inflate(inflater)
        return viewBinding.root


    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.currentTransaction.observe(viewLifecycleOwner) {
            viewBinding.tvQuantity.text = "${String.format("%.2f",it.amount)} ${it.symbol}"

            val tvStatus = viewBinding.tvStatusTransactionDetails
            when (it.isBought) {

                true -> {
                    tvStatus.text = "Bought"
                    tvStatus.setTextColor(requireContext().getColor(R.color.green))
                }

                false -> {
                    tvStatus.text = "Sold"
                    tvStatus.setTextColor(requireContext().getColor(R.color.red))
                }

                null -> {
                    tvStatus.text = "Deposit"
                    tvStatus.setTextColor(requireContext().getColor(R.color.white))
                }
            }

            viewBinding.tvTransactionsAccount.text = viewModel.currentAccount.value!!.email

            viewBinding.tvTransactionsTime.text = it.date

            viewBinding.tvTransactionsHashDetails.text = it.transactionHash

            viewBinding.tvTransactionPrice.text = when {
                it.price != null -> "${String.format("%.2f", it.price)}$"
                else -> ""
            }

        }

        viewBinding.btnBackTransactionsDetails.setOnClickListener {
            findNavController().navigateUp()
        }

    }

}