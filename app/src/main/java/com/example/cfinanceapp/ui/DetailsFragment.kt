package com.example.cfinanceapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {
private lateinit var viewBinding:FragmentDetailsBinding
private val viewModel:ViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentDetailsBinding.inflate(inflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.cryptoList.observe(viewLifecycleOwner){
            viewBinding.ivCoinLogoDetails.load(viewModel.getId())

        }

        viewBinding.btnBackDetails.findNavController().navigateUp()
    }



}