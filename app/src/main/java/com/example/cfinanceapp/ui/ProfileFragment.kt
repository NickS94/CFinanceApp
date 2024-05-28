package com.example.cfinanceapp.ui


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.databinding.DialogBoxProfileSettingsBinding
import com.example.cfinanceapp.databinding.FragmentProfileBinding
import com.example.cfinanceapp.tools.ViewModel


class ProfileFragment : Fragment() {

    private lateinit var viewBinding: FragmentProfileBinding
    private val viewModel: ViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(inflater)
        return viewBinding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        viewModel.accounts.observe(viewLifecycleOwner) {
            viewModel.findAccountByEmail(viewModel.currentAccount.value!!.email)
        }

        viewModel.currentAccount.observe(viewLifecycleOwner) {

            if (it != null) {
                viewBinding.tvName.text = it.name
                viewBinding.tvEmailProfile.text = it.email
                viewBinding.tvAccountId.text = "AccountID: ${it.id}"
            }

        }



        viewBinding.tvLogout.setOnClickListener {
            viewModel.logout()
            showToast("Logout successfully")
            findNavController().navigate(R.id.loginFragment)
        }


        viewBinding.btnEditNameProfile.setOnClickListener {
            showDialog(viewModel)
        }

    }

    /**
     * This function shows the alert dialog to change the profile name in the account.
     * @param viewModel we use this parameter because the update function works inside this alert dialog.
     */
    private fun showDialog(viewModel: ViewModel) {

        val dialogBinding = DialogBoxProfileSettingsBinding.inflate(layoutInflater)

        val dialogWindow = AlertDialog
            .Builder(requireContext(), R.style.CustomDialog)
            .setView(dialogBinding.root)
            .create()



        dialogBinding.btnConfirmDialogBox.setOnClickListener {
            val newNameText = dialogBinding.etNameProfileDialog.text.toString()
            viewModel.updateUserName(newNameText)
            showToast("Success")
            dialogWindow.dismiss()
        }

        dialogBinding.btnCancelDialogBox.setOnClickListener {
            dialogWindow.dismiss()
        }

        dialogWindow.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        ).show()

    }

}

