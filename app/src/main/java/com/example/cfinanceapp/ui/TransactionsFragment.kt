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

        viewModel.currentTransactions.observe(viewLifecycleOwner) { transactionsList ->
            viewBinding.rvTransactions.adapter = TransactionsAdapter(
                transactionsList.sortedByDescending { it.date },
                requireContext(),
                viewModel
            )
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

        viewBinding.rvTransactions.adapter = TransactionsAdapter(
            viewModel.currentTransactions.value!!.sortedByDescending { it.date },
            requireContext(),
            viewModel
        )

        viewBinding.textOptionsDropDownMenuTransactions.addTextChangedListener {
            viewBinding.rvTransactions.adapter = TransactionsAdapter(
                viewModel.filteredTransactionsList(viewBinding.textOptionsDropDownMenuTransactions.text.toString()),
                requireContext(),
                viewModel
            )
        }

        viewBinding.rvTransactions.adapter = TransactionsAdapter(
            viewModel.filteredTransactionsList(viewBinding.textOptionsDropDownMenuTransactions.text.toString()),
            requireContext(),
            viewModel
        )


    }
}