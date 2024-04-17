package com.example.cfinanceapp.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.cfinanceapp.R
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.adapters.MarketAdapter
import com.example.cfinanceapp.databinding.FragmentMarketBinding

class MarketFragment : Fragment() {

    private lateinit var viewBinding :FragmentMarketBinding
    private val viewModel :ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMarketBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCrypto()

        val recyclerView = viewBinding.rvMarketList


        viewModel.cryptoList.observe(viewLifecycleOwner){
//            recyclerView.adapter = MarketAdapter(it,viewModel)
        }
    }


}