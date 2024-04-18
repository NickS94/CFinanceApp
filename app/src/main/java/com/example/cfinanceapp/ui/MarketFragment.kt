package com.example.cfinanceapp.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cfinanceapp.R
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.adapters.MarketAdapter
import com.example.cfinanceapp.databinding.FragmentMarketBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


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

        viewModel.loadCrypto()

        val recyclerView = viewBinding.rvMarketList


        val spinnerOptions = resources.getStringArray(R.array.options)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, spinnerOptions)
        viewBinding.textOptionsDropDownMenu.setAdapter(arrayAdapter)

        viewBinding.textOptionsDropDownMenu.addTextChangedListener {
            viewModel.cryptoList.observe(viewLifecycleOwner) {
                recyclerView.adapter = MarketAdapter(
                    viewModel.filterGainers(viewBinding.textOptionsDropDownMenu.text.toString()),
                    this.requireContext(),
                    viewModel
                )

            }
        }


        viewModel.cryptoList.observe(viewLifecycleOwner) {
            recyclerView.adapter = MarketAdapter(it.data, this.requireContext(), viewModel)

        }

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

       viewBinding.searchViewMarket.setOnQueryTextFocusChangeListener { _, hasFocus ->
           when(hasFocus){
               true -> bottomNavigationView.isGone=true
               false ->bottomNavigationView.isGone=false
           }

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

    override fun onResume() {
        super.onResume()
        val spinnerOptions = resources.getStringArray(R.array.options)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, spinnerOptions)
        viewBinding.textOptionsDropDownMenu.setAdapter(arrayAdapter)




    }

}