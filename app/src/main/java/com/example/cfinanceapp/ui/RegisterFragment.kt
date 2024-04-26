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
import com.example.cfinanceapp.databinding.FragmentRegisterBinding
import com.example.cfinanceapp.tools.ViewModel



class RegisterFragment : Fragment() {

    private lateinit var viewBinding: FragmentRegisterBinding
    private val viewModel: ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentRegisterBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.accounts.observe(viewLifecycleOwner) {

            val etEmail = viewBinding.etEmailRegister.text.toString()
            val etPassword = viewBinding.etPasswordRegister.text.toString()
            val etPasswordRepeat = viewBinding.etPasswordRegisterRepeat.text.toString()
            val account = Account(email = etEmail)
            viewBinding.btnCompleteRegister.setOnClickListener {
                viewModel.registration(etEmail, etPassword) {
                    showToast("Success")
                    viewModel.createNewAccount(account)
                    findNavController().navigate(R.id.loginFragment)
                }

                        }
                    }
                }

    private fun isEmailValid(email: String): Boolean {

        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun correctPasswordLength(password: String): Boolean {
        return password.length < 8
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        ).show()
    }

}











