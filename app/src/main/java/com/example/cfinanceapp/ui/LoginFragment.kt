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

                viewModel.authenticateUser(etEmail, etPassword).observe(viewLifecycleOwner) { isAuthenticated ->
                    if (isAuthenticated) {
                        findNavController().navigate(R.id.homeFragment)
                    } else {
                        showToast("Ensure that your EMAIL information OR PASSWORD is correct")
                    }
                }
            }
        }
        viewBinding.createAccountClickable.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
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