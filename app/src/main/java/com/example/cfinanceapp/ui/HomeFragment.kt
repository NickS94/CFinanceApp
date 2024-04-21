package com.example.cfinanceapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cfinanceapp.tools.ViewModel
import com.example.cfinanceapp.adapters.HotListAdapter
import com.example.cfinanceapp.adapters.WatchListAdapter
import com.example.cfinanceapp.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var viewBinding: FragmentHomeBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentHomeBinding.inflate(inflater)
        return viewBinding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)






        val recyclerViewHot = viewBinding.rvHotMarketList
        viewModel.cryptoList.observe(viewLifecycleOwner) {
            recyclerViewHot.adapter = HotListAdapter(viewModel.loadHotList(),viewModel)
        }


        val recyclerViewWatchList = viewBinding.rvWatchlist

        viewModel.cryptoWatchList.observe(viewLifecycleOwner) { watchlist ->
            recyclerViewWatchList.adapter = WatchListAdapter(watchlist,viewModel)
        }


    }









}