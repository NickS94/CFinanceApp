package com.example.cfinanceapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.adapters.TransactionsAdapter
import com.example.cfinanceapp.databinding.FragmentTransactionsBinding
import com.example.cfinanceapp.tools.ViewModel


class TransactionsFragment : Fragment() {

    private lateinit var viewBinding: FragmentTransactionsBinding
    private val viewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentTransactionsBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TransactionsAdapter(context = this.requireContext(), viewModel = viewModel)

        viewBinding.rvTransactions.adapter = adapter


        viewModel.currentTransactions.observe(viewLifecycleOwner){
            adapter.submitListTransactions(viewModel.currentTransactions.value!!)
        }


        viewBinding.btnBackTransactions.setOnClickListener {
            findNavController().navigateUp()
        }


    }


}