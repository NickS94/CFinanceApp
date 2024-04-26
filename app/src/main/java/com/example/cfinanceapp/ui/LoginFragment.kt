package com.example.cfinanceapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.databinding.FragmentLoginBinding
import com.example.cfinanceapp.tools.ViewModel

class LoginFragment : Fragment() {

    private lateinit var viewBinding: FragmentLoginBinding
    private val viewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentLoginBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.accounts.observe(viewLifecycleOwner) {

            viewBinding.btnLogin.setOnClickListener {
                val etEmail = viewBinding.etLogInEmail.text.toString()
                val etPassword = viewBinding.etPasswordLogin.text.toString()
                val account = Account(email = etEmail)
                when {
                    etEmail.isEmpty() -> showToast("Please give your EMAIL.")
                    etPassword.isEmpty() -> showToast("Please give your password.")
                    !viewModel.isAccountAlreadyRegistered(etEmail) -> showToast("You are not Registered , Please REGISTER an ACCOUNT.")
                    else -> viewModel.loginAuthentication(etEmail, etPassword) {
                        viewModel.getCurrentAccount(account)
                        showToast("Welcome ${account.name}!")
                        findNavController().navigate(R.id.homeFragment)
                    }

                }
            }

        }

        viewBinding.createAccountClickable.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }


    }

    fun showToast(message: String) {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

}