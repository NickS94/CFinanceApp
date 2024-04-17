package com.example.cfinanceapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isVisible


import androidx.fragment.app.activityViewModels
import com.example.cfinanceapp.R
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.adapters.MarketAdapter
import com.example.cfinanceapp.databinding.FragmentMarketBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.internal.notify


class MarketFragment : Fragment() {

    private lateinit var viewBinding: FragmentMarketBinding
    private val viewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentMarketBinding.inflate(inflater)
        return viewBinding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val recyclerView = viewBinding.rvMarketList


        viewModel.cryptoList.observe(viewLifecycleOwner) {
            recyclerView.adapter = MarketAdapter(it.data, this.requireContext(), viewModel)

        }




        viewBinding.searchViewMarket.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.cryptoList.observe(viewLifecycleOwner) {
                    recyclerView.adapter =
                        MarketAdapter(viewModel.search(newText.toString()), context!!, viewModel)
                }
                return true
            }


        })






    }
}