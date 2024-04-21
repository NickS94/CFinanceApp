package com.example.cfinanceapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.tools.ViewModel
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.databinding.FragmentDetailsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class DetailsFragment : Fragment() {
private lateinit var viewBinding:FragmentDetailsBinding
private val viewModel: ViewModel by activityViewModels()
    private val data : DetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentDetailsBinding.inflate(inflater)
        return viewBinding.root
    }



    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var frameText = ""
        val coin = data.cryptoData




        var selectedButton: Button? = null

        viewModel.cryptoList.observe(viewLifecycleOwner){
            loadChart(coin,frameText)

            viewBinding.btn1h.setOnClickListener {

                selectedButton?.setBackgroundResource(android.R.color.transparent)
                selectedButton = viewBinding.btn1h
                viewBinding.btn1h.setBackgroundResource(R.drawable.round_transparent)

                frameText = "1H"
                loadChart(coin,frameText)

            }
            viewBinding.btn24h.setOnClickListener {

                selectedButton?.setBackgroundResource(android.R.color.transparent)
                selectedButton = viewBinding.btn24h
                viewBinding.btn24h.setBackgroundResource(R.drawable.round_transparent)

                frameText = "D"
                loadChart(coin,frameText)
            }
            viewBinding.btnWeek.setOnClickListener {

                selectedButton?.setBackgroundResource(android.R.color.transparent)
                selectedButton = viewBinding.btnWeek
                viewBinding.btnWeek.setBackgroundResource(R.drawable.round_transparent)

                frameText = "W"
                loadChart(coin,frameText)
            }


            viewBinding.ivCoinLogoDetails.load(viewModel.getCoinLogo(coin.id.toString()))

            viewBinding.tvCoinNameDetails.text = coin.name

            viewBinding.tvCoinSymbolDetails.text = coin.symbol

            viewBinding.tvChangePercentageDetails.text =
                "${String.format("%.02f", coin.quote.usdData.percentChange24h)}%"

            viewBinding.tvCurrentPriceDetails.text = "${String.format("%.02f",coin.quote.usdData.price)}$"

            when{
                coin.quote.usdData.percentChange24h > 0 ->{
                    viewBinding.tvChangePercentageDetails.setBackgroundResource(R.drawable.rounded_percentage_up)

                }
                coin.quote.usdData.percentChange24h < 0 -> {
                    viewBinding.tvChangePercentageDetails.setBackgroundResource(R.drawable.rounded_percentage_down)

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


            val volume24h = coin.quote.usdData.volume24h
            val formatedVolume = if (volume24h >= 1_000_000_000) {
                String.format("%.2f Bil", volume24h / 1_000_000_000)
            } else {
                String.format("%.2f Mil.", volume24h / 1_000_000)
            }
            viewBinding.tvVolume24h.text = "Volume 24H: $formatedVolume"


            val circulatingSupply = coin.circulatingSupply
            val formattedCirculatingSupply = if (circulatingSupply >= 1_000_000_000) {
                String.format("%.2f Bil.", circulatingSupply / 1_000_000_000)
            } else {
                String.format("%.2f Mil.", circulatingSupply / 1_000_000)
            }
            viewBinding.tvCirculatingSupplyDetails.text = "Circulating: $formattedCirculatingSupply"


            val totalSupply = coin.totalSupply
            val formattedTotalSupply = if (totalSupply >= 1_000_000_000) {
                String.format("%.2f Bil.", totalSupply / 1_000_000_000)
            } else {
                String.format("%.2f Mil.", totalSupply / 1_000_000)
            }
            viewBinding.tvTotalSupplyDetail.text = "Circulating: $formattedTotalSupply"



            if (coin.maxSupply == null) {
                viewBinding.tvMaxSupplyDetail.text = "Max: Unlimited"
            } else {
                val maxSupply = coin.maxSupply
                val formattedMaxSupply = if (maxSupply >= 1_000_000_000) {
                    String.format("%.2f Bil.", maxSupply / 1_000_000_000)
                } else {
                    String.format("%.2f Mil.", maxSupply / 1_000_000)
                }
                viewBinding.tvMaxSupplyDetail.text = "Max: $formattedMaxSupply"
            }


            val marketCap = coin.quote.usdData.marketCap
            val formattedMarketCap = if (marketCap >= 1_000_000_000) {
                String.format("%.2f Bil.", marketCap / 1_000_000_000)
            } else {
                String.format("%.2f Mil.", marketCap / 1_000_000)
            }
            viewBinding.tvMarketCapDetailsCard.text = "Market Cap \n $formattedMarketCap $"



        }

        viewBinding.ivFavorite.setOnClickListener {

            viewModel.addToWatchlist(coin)
        }




        viewBinding.btnBackDetails.setOnClickListener {
            findNavController().navigateUp()
        }



    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadChart(coin: CryptoCurrency, timeframe:String ){


        viewBinding.wvChartDetails.settings.javaScriptEnabled = true
        viewBinding.wvChartDetails.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        viewBinding.wvChartDetails.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol="+coin.symbol+"usd&interval="+timeframe+"&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term"

        )
    }

    override fun onResume() {
        super.onResume()
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.isGone = true
    }


}