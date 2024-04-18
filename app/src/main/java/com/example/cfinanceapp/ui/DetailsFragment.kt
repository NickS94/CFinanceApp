package com.example.cfinanceapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.ViewModel
import com.example.cfinanceapp.databinding.FragmentDetailsBinding


class DetailsFragment : Fragment() {
private lateinit var viewBinding:FragmentDetailsBinding
private val viewModel:ViewModel by activityViewModels()
    private val data : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDetailsBinding.inflate(inflater)
        return viewBinding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val coin = data.cryptoData

        viewModel.cryptoList.observe(viewLifecycleOwner){
            viewBinding.ivCoinLogoDetails.load(viewModel.getCoinLogo(coin.id.toString()))
            viewBinding.tvCoinNameDetails.text = coin.name
            viewBinding.tvCurrentPriceDetails.text = "${String.format("%.02f",coin.quote.usdData.price)}$"
            viewBinding.tvChangePercentageDetails.text = "${String.format("%.02f",coin.quote.usdData.percentChange24h)}%"
            when{
                coin.quote.usdData.percentChange24h > 0 ->{
                    viewBinding.tvChangePercentageDetails.setTextColor(requireContext().getColor(R.color.green))
                    viewBinding.ivArrowDetails.setImageResource(R.drawable.price_up_arrow)
                }
                coin.quote.usdData.percentChange24h < 0 -> {
                    viewBinding.tvChangePercentageDetails.setTextColor(requireContext().getColor(R.color.red))
                    viewBinding.ivArrowDetails.setImageResource(R.drawable.price_down_arrow)
                }
            }
            viewBinding.tv1hChangeDetail.text = "${String.format("%.02f",coin.quote.usdData.percentChange1h)}%"

            when{
                coin.quote.usdData.percentChange1h > 0 ->{
                    viewBinding.tv1hChangeDetail.setTextColor(requireContext().getColor(R.color.green))

                }
                coin.quote.usdData.percentChange1h < 0 -> {
                    viewBinding.tv1hChangeDetail.setTextColor(requireContext().getColor(R.color.red))

                }
            }



            viewBinding.tv24hChangeDetail.text = "${String.format("%.02f",coin.quote.usdData.percentChange24h)}%"
            when{
                coin.quote.usdData.percentChange24h > 0 ->{
                    viewBinding.tv24hChangeDetail.setTextColor(requireContext().getColor(R.color.green))
                }
                coin.quote.usdData.percentChange24h < 0 -> {
                    viewBinding.tv24hChangeDetail.setTextColor(requireContext().getColor(R.color.red))
                }
            }

            viewBinding.tv7dChangeDetail.text =  "${String.format("%.02f",coin.quote.usdData.percentChange7d)}%"
            when{
                coin.quote.usdData.percentChange7d > 0 ->{
                    viewBinding.tv7dChangeDetail.setTextColor(requireContext().getColor(R.color.green))
                }
                coin.quote.usdData.percentChange7d < 0 -> {
                    viewBinding.tv7dChangeDetail.setTextColor(requireContext().getColor(R.color.red))
                }
            }

            viewBinding.tvCirculatingSupplyDetails.text ="Circulating:${String.format("%.1f",coin.circulatingSupply)}Mil."
            viewBinding.tvTotalSupplyDetail.text = "Total:${String.format("%.1f",coin.totalSupply)}Mil."
            viewBinding.tvMaxSupplyDetail.text = "Max:${String.format("%.1f",coin.maxSupply)}Mil."
            viewBinding.tvMarketCapDetailsCard.text = "Market Cap \n${String.format("%.1f",coin.quote.usdData.marketCap)}"


        }


        viewBinding.btnBackDetails.setOnClickListener {
            findNavController().navigateUp()
        }






    }
}