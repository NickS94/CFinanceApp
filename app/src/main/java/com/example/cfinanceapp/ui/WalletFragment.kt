package com.example.cfinanceapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.cfinanceapp.R
import com.example.cfinanceapp.data.models.Wallet
import com.example.cfinanceapp.databinding.FragmentWalletBinding
import com.example.cfinanceapp.tools.ViewModel

class WalletFragment : Fragment() {

    private lateinit var viewBinding: FragmentWalletBinding
    private val viewModel : ViewModel by activityViewModels()

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
            viewModel.createNewWallet()
        }

        viewModel.wallets.observe(viewLifecycleOwner){


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