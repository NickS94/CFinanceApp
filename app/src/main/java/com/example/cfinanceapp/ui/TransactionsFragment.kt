package com.example.cfinanceapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.adapters.TransactionsAdapter
import com.example.cfinanceapp.databinding.FragmentTransactionsBinding
import com.example.cfinanceapp.tools.ViewModel


class TransactionsFragment : Fragment() {

    private lateinit var viewBinding: FragmentTransactionsBinding
    private val viewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentTransactionsBinding.inflate(inflater)
        return viewBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.findTransactionsByWalletId()

        val spinnerOptions = resources.getStringArray(R.array.optionsTransactions)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, spinnerOptions)
        viewBinding.textOptionsDropDownMenuTransactions.setAdapter(
            arrayAdapter
        )

        val adapter = TransactionsAdapter(context = this.requireContext(), viewModel = viewModel)
        viewBinding.rvTransactions.adapter = adapter


        viewModel.currentTransactions.observe(viewLifecycleOwner) {
            adapter.submitListTransactions(viewModel.currentTransactions.value!!.sortedByDescending { it.date })
        }

        viewBinding.btnBackTransactions.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    // Here i also use the onResume function so i can save the current state of filtering.
    override fun onResume() {
        super.onResume()

        val spinnerOptions = resources.getStringArray(R.array.optionsTransactions)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, spinnerOptions)
        viewBinding.textOptionsDropDownMenuTransactions.setAdapter(
            arrayAdapter
        )

        val adapter = TransactionsAdapter(context = this.requireContext(), viewModel = viewModel)
        viewBinding.rvTransactions.adapter = adapter

        viewBinding.textOptionsDropDownMenuTransactions.addTextChangedListener {
            adapter.submitListTransactions(viewModel.filteredTransactionsList(viewBinding.textOptionsDropDownMenuTransactions.text.toString()))
        }

        adapter.submitListTransactions(viewModel.filteredTransactionsList(viewBinding.textOptionsDropDownMenuTransactions.text.toString()))


    }
}