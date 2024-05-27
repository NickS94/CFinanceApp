package com.example.cfinanceapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.cfinanceapp.R
import com.example.cfinanceapp.tools.ViewModel
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.databinding.FragmentDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class DetailsFragment : Fragment() {
    private lateinit var viewBinding: FragmentDetailsBinding
    private val viewModel: ViewModel by activityViewModels()
    private var isFavorite: Boolean = false

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

        var selectedButton: Button? = null

        viewModel.findAssetsByWalletId()

        viewModel.findWalletByUserId()

        viewModel.currentCrypto.observe(viewLifecycleOwner) { cryptoCurrency ->


            loadChart(cryptoCurrency, "D")

            viewBinding.ivCoinLogoDetails.load(viewModel.getCoinLogo(cryptoCurrency.id.toString()))

            viewBinding.tvCoinNameDetails.text = cryptoCurrency.name

            viewBinding.tvCoinSymbolDetails.text = cryptoCurrency.symbol

            viewBinding.tvChangePercentageDetails.text =
                "${String.format("%.02f", cryptoCurrency.quote.usdData.percentChange24h)}%"

            viewBinding.tvCurrentPriceDetails.text =
                "${String.format("%.02f", cryptoCurrency.quote.usdData.price)}$"

            when {
                cryptoCurrency.quote.usdData.percentChange24h > 0 -> {
                    viewBinding.tvChangePercentageDetails.setBackgroundResource(R.drawable.rounded_percentage_up)

                }

                cryptoCurrency.quote.usdData.percentChange24h < 0 -> {
                    viewBinding.tvChangePercentageDetails.setBackgroundResource(R.drawable.rounded_percentage_down)

                }
            }
            viewBinding.tv1hChangeDetail.setChangeText(cryptoCurrency.quote.usdData.percentChange1h)

            viewBinding.tv24hChangeDetail.setChangeText(cryptoCurrency.quote.usdData.percentChange24h)

            viewBinding.tv7dChangeDetail.setChangeText(cryptoCurrency.quote.usdData.percentChange7d)

            val volume24h = cryptoCurrency.quote.usdData.volume24h
            val formattedVolume = volume24h.formatVolume()
            viewBinding.tvVolume24h.text = "Volume 24H: $formattedVolume"


            val circulatingSupply = cryptoCurrency.circulatingSupply
            val formattedCirculatingSupply = circulatingSupply.formatVolume()
            viewBinding.tvCirculatingSupplyDetails.text = "Circulating: $formattedCirculatingSupply"


            val totalSupply = cryptoCurrency.totalSupply
            val formattedTotalSupply = totalSupply.formatVolume()
            viewBinding.tvTotalSupplyDetail.text = "Total: $formattedTotalSupply"


            val marketCap = cryptoCurrency.quote.usdData.marketCap
            val formattedMarketCap = marketCap.formatVolume()
            viewBinding.tvMarketCapDetailsCard.text = "Market Cap \n $formattedMarketCap $"


            if (cryptoCurrency.maxSupply == null) {
                viewBinding.tvMaxSupplyDetail.text = "Max: Unlimited"
            } else {
                val maxSupply = cryptoCurrency.maxSupply
                val formattedMaxSupply = maxSupply.formatVolume()
                viewBinding.tvMaxSupplyDetail.text = "Max: $formattedMaxSupply"
            }

            isFavorite = viewModel.isFavorite(viewModel.currentCrypto.value!!)

        }
        viewBinding.btn1h.setOnClickListener {

            selectedButton?.setBackgroundResource(android.R.color.transparent)
            selectedButton = viewBinding.btn1h
            viewBinding.btn1h.setBackgroundResource(R.drawable.round_transparent)


            loadChart(viewModel.currentCrypto.value!!, viewBinding.btn1h.text.toString())

        }
        viewBinding.btn24h.setOnClickListener {

            selectedButton?.setBackgroundResource(android.R.color.transparent)
            selectedButton = viewBinding.btn24h
            viewBinding.btn24h.setBackgroundResource(R.drawable.round_transparent)

            loadChart(viewModel.currentCrypto.value!!, viewBinding.btn24h.text.toString())
        }
        viewBinding.btnWeek.setOnClickListener {

            selectedButton?.setBackgroundResource(android.R.color.transparent)
            selectedButton = viewBinding.btnWeek
            viewBinding.btnWeek.setBackgroundResource(R.drawable.round_transparent)

            loadChart(viewModel.currentCrypto.value!!, viewBinding.btnWeek.text.toString())
        }


        if (viewModel.isFavorite(viewModel.currentCrypto.value!!)) {
            viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_enabled)
        } else {
            viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_disable)
        }

        viewBinding.ivFavorite.setOnClickListener {

            isFavorite = !isFavorite

            viewModel.addToWatchlist(viewModel.currentCrypto.value!!)

            if (isFavorite) {
                viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_enabled)
            } else {
                viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_disable)
            }

        }

        viewBinding.btnBuyDetails.setOnClickListener {
            if (viewModel.currentWallet.value != null) {
                showBuyCryptoDialog(viewModel.currentCrypto.value!!, viewModel)
            } else {
                showToast("Please CREATE a WALLET for transactions")
            }
        }

        viewBinding.btnSell.setOnClickListener {
            if (viewModel.currentWallet.value != null) {
                showSellCryptoDialog(viewModel.currentCrypto.value!!, viewModel)
            } else {
                showToast("Please CREATE a WALLET for transactions")
            }
        }

        viewBinding.btnBackDetails.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadChart(coin: CryptoCurrency, timeframe: String) {


        viewBinding.wvChartDetails.settings.javaScriptEnabled = true
        viewBinding.wvChartDetails.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        viewBinding.wvChartDetails.webViewClient = WebViewClient()
        viewBinding.wvChartDetails.loadUrl(
            "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=" + coin.symbol + "usd&interval=" + timeframe +
                    "&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]" +
                    "&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}" +
                    "&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term"
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        ).show()

    }

    private fun Double.formatVolume(): String {
        return if (this >= 1_000_000_000) {
            String.format("%.2f Bil", this / 1_000_000_000)
        } else {
            String.format("%.2f Mil.", this / 1_000_000)
        }
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

    @SuppressLint("InflateParams")
    private fun showBuyCryptoDialog(cryptoCurrency: CryptoCurrency, viewModel: ViewModel) {
        val dialog = BottomSheetDialog(requireContext())
        val viewLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog_buy_layout, null)
        val btnConfirm = viewLayout.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = viewLayout.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnMax = viewLayout.findViewById<AppCompatButton>(R.id.btnMax)
        val etAmount = viewLayout.findViewById<TextInputEditText>(R.id.etAmount)

        viewModel.resetMaxAmount()

        viewModel.findAssetsByWalletId()

        viewModel.maxAmount.observe(viewLifecycleOwner) { maxAmount ->
            etAmount.setText(maxAmount.toString())
        }

        btnMax.setOnClickListener {
            viewModel.maxToBuy(cryptoCurrency)
        }
        btnConfirm.setOnClickListener {
            val amountText = etAmount.text.toString()
            when {
                amountText.isNotEmpty() && viewModel.isEnoughFiat(
                    amountText.toDouble()
                ) && amountText.toDouble() > 0 -> {
                    viewModel.updateOrInsertCryptoCurrencyAmounts(
                        amountText.toDouble(),
                        cryptoCurrency
                    )
                    showToast("Bought $amountText ${cryptoCurrency.name}")
                    dialog.dismiss()
                }

                amountText.isEmpty() || amountText.toDouble() <= 0 -> showToast("Please enter a valid amount")

                !viewModel.isEnoughFiat(
                    amountText.toDouble()
                ) -> showToast("Insufficient funds ")
            }

        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(viewLayout)
        dialog.show()
    }

    @SuppressLint("InflateParams")
    private fun showSellCryptoDialog(cryptoCurrency: CryptoCurrency, viewModel: ViewModel) {
        val dialog = BottomSheetDialog(requireContext())
        val viewLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog_buy_layout, null)
        val btnConfirm = viewLayout.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = viewLayout.findViewById<AppCompatButton>(R.id.btnCancel)
        val etAmount = viewLayout.findViewById<TextInputEditText>(R.id.etAmount)
        val btnMax = viewLayout.findViewById<AppCompatButton>(R.id.btnMax)
        val textInputHint = viewLayout.findViewById<TextInputLayout>(R.id.textInputLayoutAmount)

        textInputHint.hint = getText(R.string.amountSell)

        viewModel.resetMaxAmount()

        viewModel.findAssetsByWalletId()

        viewModel.maxAmount.observe(viewLifecycleOwner) { maxAmount ->
            etAmount.setText(maxAmount.toString())
        }

        btnMax.setOnClickListener {
            viewModel.maxOfAsset(cryptoCurrency).toString()
        }

        btnConfirm.setOnClickListener {
            val amountText = etAmount.text.toString()
            when {
                amountText.isNotEmpty() && viewModel.isEnoughCrypto(
                    amountText.toDouble()
                ) && amountText.toDouble() > 0 -> {
                    viewModel.sellCryptoCurrencyAsset(
                        cryptoCurrency,
                        amountText.toDouble()
                    )
                    showToast("Sold $amountText ${cryptoCurrency.name}")
                    dialog.dismiss()
                }

                amountText.isEmpty() || amountText.toDouble() <= 0 -> showToast("Please enter a valid amount")

                !viewModel.isEnoughCrypto(
                    amountText.toDouble()
                ) -> showToast("Insufficient funds ")
            }

        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.setContentView(viewLayout)
        dialog.show()
    }

}