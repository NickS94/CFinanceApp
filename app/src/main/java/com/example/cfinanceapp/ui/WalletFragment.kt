package com.example.cfinanceapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.cfinanceapp.adapters.AssetsAdapter
import com.example.cfinanceapp.databinding.FragmentWalletBinding
import com.example.cfinanceapp.tools.ViewModel

class WalletFragment : Fragment() {

    private lateinit var viewBinding: FragmentWalletBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentWalletBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewBinding.btnCreateNewWallet.setOnClickListener {
            if (viewModel.currentWallet.value == null)
            {
                viewModel.createNewWallet()
                showToast("Your WALLET have been CREATED SUCCESSFULLY ")
            }else{
                showToast("You Already have a wallet created")
            }
        }

        val adapter = AssetsAdapter(viewModel = viewModel)
        viewBinding.rvAssetsWallet.adapter = adapter

       viewModel.currentAccount.observe(viewLifecycleOwner){
           viewModel.findWalletByUserId(it.id)
       }

        viewModel.currentWallet.observe(viewLifecycleOwner){



        }



    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }


}