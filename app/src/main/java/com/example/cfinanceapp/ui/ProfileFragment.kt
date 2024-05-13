package com.example.cfinanceapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cfinanceapp.R
import com.example.cfinanceapp.databinding.FragmentProfileBinding
import com.example.cfinanceapp.tools.ViewModel


class ProfileFragment : Fragment() {

    private lateinit var viewBinding: FragmentProfileBinding
    private val viewModel : ViewModel by activityViewModels()

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

        viewModel.currentAccount.observe(viewLifecycleOwner){

            if (it != null ){
                viewBinding.tvName.text = it.name
                viewBinding.tvEmailProfile.text = it.email
                viewBinding.tvAccountId.text = "AccountID: ${it.id}"
            }
            viewBinding.tvLogout.setOnClickListener {
                viewModel.logout()
                findNavController().navigate(R.id.loginFragment)
            }

        }


    }

}