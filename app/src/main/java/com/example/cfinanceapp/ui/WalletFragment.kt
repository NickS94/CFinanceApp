package com.example.cfinanceapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.adapters.AssetsAdapter
import com.example.cfinanceapp.databinding.FragmentWalletBinding
import com.example.cfinanceapp.tools.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText

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

        val adapter = AssetsAdapter(viewModel = viewModel)
        viewBinding.rvAssetsWallet.adapter = adapter

        viewModel.currentAccount.observe(viewLifecycleOwner) { account ->
            if (viewModel.wallets.value != null) {

            }
            viewModel.findWalletByUserId(account.id)
            viewModel.findAccountByEmail(account.email)
            viewBinding.btnCreateNewWallet.setOnClickListener {
                if (viewModel.currentWallet.value?.accountId != account.id) {
                    viewModel.createNewWallet()
                    showToast("Your WALLET have been CREATED SUCCESSFULLY ")
                } else {
                    showToast("You Already have a wallet created")
                }
            }
        }

        viewModel.wallets.observe(viewLifecycleOwner) {
            if (viewModel.currentAccount.value != null) {
                viewModel.findWalletByUserId(viewModel.currentAccount.value!!.id)
            }
        }

        viewModel.assets.observe(viewLifecycleOwner) {
            if (viewModel.currentWallet.value != null) {
                viewModel.findAssetsByWalletId(viewModel.currentWallet.value!!.id)
            }

        }


        viewModel.currentAssets.observe(viewLifecycleOwner) {
            if (viewModel.currentWallet.value != null) {
                adapter.submitList(viewModel.currentAssets.value!!)
                viewBinding.currentBalanceText.stringFormat(viewModel.currentBalance())
            }

        }



        viewBinding.btnDeposit.setOnClickListener {

            if (viewModel.currentWallet.value != null) {
                showBuyCryptoDialog(viewModel)

            } else {
                showToast("You need to CREATE WALLET first")
            }
        }



        viewBinding.btnHistory.setOnClickListener {
            when {
                viewModel.currentWallet.value != null -> {
                    viewModel.findTransactionsByWalletId(viewModel.currentWallet.value!!.id)
                    findNavController().navigate(R.id.transactionsFragment)
                }

                else -> showToast("You have to CREATE a WALLET first")

            }


        }

    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun TextView.stringFormat(balance: Double) {
        val formattedText = String.format("%.2f $", balance)
        text = formattedText
    }


    @SuppressLint("InflateParams")
    private fun showBuyCryptoDialog(viewModel: ViewModel) {
        val dialog = BottomSheetDialog(requireContext())
        val viewLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog_layout, null)
        val btnConfirm = viewLayout.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = viewLayout.findViewById<AppCompatButton>(R.id.btnCancel)
        val etAmount = viewLayout.findViewById<TextInputEditText>(R.id.etAmount)

        btnConfirm.setOnClickListener {
            val amountText = etAmount.text
            if (amountText!!.isNotEmpty()) {
                val amount = amountText.toString().toDouble()
                viewModel.updateOrInsertFiatCurrencyAmounts(amount)
                showToast("Deposit $amount$ COMPLETED ")
                dialog.dismiss()
            } else {
                showToast("Please enter a valid amount")
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(viewLayout)
        dialog.show()
    }

}