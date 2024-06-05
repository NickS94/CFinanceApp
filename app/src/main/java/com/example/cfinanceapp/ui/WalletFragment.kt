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
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.adapters.AssetsAdapter
import com.example.cfinanceapp.databinding.FragmentWalletBinding
import com.example.cfinanceapp.tools.ViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class WalletFragment : Fragment() {

    private lateinit var viewBinding: FragmentWalletBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.loadWalletData()

        viewBinding = FragmentWalletBinding.inflate(inflater)
        return viewBinding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentTransactions.observe(viewLifecycleOwner) {
            if (viewModel.currentWallet.value != null){
                viewBinding.currentBalanceText.text =
                    "${String.format("%.2f", viewModel.currentBalance())}$"
                profitLossCount()
                viewBinding.tvProfit.stringFormat(viewModel.profitOrLoss())
            }


        }

        viewModel.currentAssets.observe(viewLifecycleOwner) {
            if (viewModel.currentWallet.value != null){
                viewBinding.rvAssetsWallet.adapter = AssetsAdapter(it, viewModel, requireContext())
            }
        }

        viewBinding.btnCreateNewWallet.setOnClickListener {
            if (viewModel.currentWallet.value?.accountId != viewModel.currentAccount.value!!.id) {
                viewModel.createNewWallet()
                showToast("Your WALLET have been CREATED SUCCESSFULLY ")
            } else {
                showToast("You Already have a wallet created")
            }
        }

        viewBinding.btnDeposit.setOnClickListener {
            if (viewModel.currentWallet.value != null) {
                showDepositDialog(viewModel)
            } else {
                showToast("You need to CREATE WALLET first")
            }
        }

        viewBinding.btnHistory.setOnClickListener {
            when {
                viewModel.currentWallet.value != null -> {
                    viewModel.findTransactionsByWalletId()
                    findNavController().navigate(R.id.transactionsFragment)
                }

                else -> showToast("You have to CREATE a WALLET first")
            }
        }

        viewBinding.btnSend.setOnClickListener {
            showToast("Coming Soon!")
        }
    }

    private fun profitLossCount() {
        when {
            viewModel.profitOrLoss() > 0 -> viewBinding.tvProfit.setTextColor(
                requireContext().getColor(
                    R.color.green
                )
            )

            viewModel.profitOrLoss() < 0 -> viewBinding.tvProfit.setTextColor(
                requireContext().getColor(
                    R.color.red
                )
            )

            else -> viewBinding.tvProfit.setTextColor(requireContext().getColor(R.color.white))
        }
        viewBinding.tvChangePercentageAssets.setChangeText(viewModel.profitOrLossPercentage())
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun TextView.stringFormat(balance: Double) {
        val formattedText = String.format("%.2f $ P/L", balance)
        text = formattedText
    }

    /**
     * This function shows the bottom sheet window for the deposit transaction.
     * @param viewModel is because the update or insert amount happens inside the dialog.
     */
    @SuppressLint("InflateParams")
    private fun showDepositDialog(viewModel: ViewModel) {
        viewModel.loadWalletData()
        val dialog = BottomSheetDialog(requireContext())
        val viewLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog_buy_layout, null)
        val btnConfirm = viewLayout.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = viewLayout.findViewById<AppCompatButton>(R.id.btnCancel)
        val etAmount = viewLayout.findViewById<TextInputEditText>(R.id.etAmount)
        val btnMax = viewLayout.findViewById<AppCompatButton>(R.id.btnMax)
        val textInputHint = viewLayout.findViewById<TextInputLayout>(R.id.textInputLayoutAmount)

        btnMax.visibility = View.GONE

        textInputHint.hint = getText(R.string.amount)

        btnConfirm.setOnClickListener {
            val amountText = etAmount.text
            if (amountText!!.isNotEmpty()) {
                val amount = amountText.toString().toDouble()
                viewModel.updateOrInsertFiatCurrencyAmounts(amount)
                showToast("Deposit $amount$ COMPLETED,")
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

    private fun TextView.setChangeText(change: Double) {
        val formattedChange = String.format("%.2f", change)
        text = formattedChange

        val colorResId = when {
            change > 0 -> R.color.green
            change < 0 -> R.color.red
            else -> android.R.color.white
        }

        setTextColor(ContextCompat.getColor(context, colorResId))
    }

}