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

    //This variable checks together with our isFavorite function from view model if the current crypto is in the watchlist.
    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.loadWalletData()

        viewBinding = FragmentDetailsBinding.inflate(inflater)
        return viewBinding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // This variable sets the button to null every time we get out of our detail fragment and gives it value every time we press the specific button we want.
        // I used it for the enabled animation on the chart timeframe buttons.
        var selectedButton: Button? = null



        viewModel.currentCrypto.observe(viewLifecycleOwner) { cryptoCurrency ->

            loadChart(cryptoCurrency, "D")

            viewBinding.ivCoinLogoDetails.load(viewModel.getCoinLogo(cryptoCurrency.id.toString()))

            viewBinding.tvCoinNameDetails.text = cryptoCurrency.name

            viewBinding.tvCoinSymbolDetails.text = cryptoCurrency.symbol

            viewBinding.tvChangePercentageDetails.text =
                "${String.format("%.2f", cryptoCurrency.quote.usdData.percentChange24h)}%"

            viewBinding.tvCurrentPriceDetails.text =
                "${String.format("%.2f", cryptoCurrency.quote.usdData.price)}$"

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

            isFavorite = viewModel.isFavorite(cryptoCurrency)




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


        // Sets the favorite drawable.
        if (viewModel.isFavorite(viewModel.currentCrypto.value!!)) {
            viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_enabled)
        } else {
            viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_disable)
        }

        viewBinding.ivFavorite.setOnClickListener {

            // Changes the value of isFavorite variable from true to false and vice versa.
            isFavorite = !isFavorite

            viewModel.addToWatchlist(viewModel.currentCrypto.value!!)

            if (isFavorite) {
                viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_enabled)
            } else {
                viewBinding.ivFavorite.setImageResource(R.drawable.favorite_icon_disable)
            }
        }



        viewBinding.btnBackDetails.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    /**
     * This function loads a web view with a chart from Trading View website based on the coin symbol
     * and the timeframe we picked.
     * @param coin is for the crypto we are looking for.
     * @param timeframe is for the time frame we want to watch for.
     */
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

    /**
     * This function shows a toast with a specific text we give .
     * @param message is for the TOAST message we want to show.
     */
    private fun showToast(message: String) {
        Toast.makeText(
            context, message, Toast.LENGTH_SHORT
        ).show()

    }

    /**
     * This function is to format a long double number.
     */
    private fun Double.formatVolume(): String {
        return if (this >= 1_000_000_000) {
            String.format("%.2f Bil", this / 1_000_000_000)
        } else {
            String.format("%.2f Mil.", this / 1_000_000)
        }
    }

    /**
     * This function is to change the format of a text view and the color.
     * @param change is for the text in our text view.
     */
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

    /**
     * This function is to show the bottom sheet window for the buy transaction .
     * Also make the transaction inside it from the view model.
     * @param cryptoCurrency is for the crypto we want to transact.
     * @param viewModel is for our view model.
     */
    @SuppressLint("InflateParams")
    private fun showBuyCryptoDialog(cryptoCurrency: CryptoCurrency, viewModel: ViewModel) {

        viewModel.loadWalletData()

        val dialog = BottomSheetDialog(requireContext())
        val viewLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog_buy_layout, null)
        val btnConfirm = viewLayout.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = viewLayout.findViewById<AppCompatButton>(R.id.btnCancel)
        val btnMax = viewLayout.findViewById<AppCompatButton>(R.id.btnMax)
        val etAmount = viewLayout.findViewById<TextInputEditText>(R.id.etAmount)

        viewModel.resetMaxAmount()

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

    /**
     * This function does the same thing as the last one but for a sell transaction.
     */
    @SuppressLint("InflateParams")
    private fun showSellCryptoDialog(cryptoCurrency: CryptoCurrency, viewModel: ViewModel) {

        viewModel.loadWalletData()

        val dialog = BottomSheetDialog(requireContext())
        val viewLayout = layoutInflater.inflate(R.layout.bottom_sheet_dialog_buy_layout, null)
        val btnConfirm = viewLayout.findViewById<AppCompatButton>(R.id.btnConfirm)
        val btnCancel = viewLayout.findViewById<AppCompatButton>(R.id.btnCancel)
        val etAmount = viewLayout.findViewById<TextInputEditText>(R.id.etAmount)
        val btnMax = viewLayout.findViewById<AppCompatButton>(R.id.btnMax)
        val textInputHint = viewLayout.findViewById<TextInputLayout>(R.id.textInputLayoutAmount)

        textInputHint.hint = getText(R.string.amountSell)

        viewModel.resetMaxAmount()


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
                    amountText.toDouble(),cryptoCurrency
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
                    amountText.toDouble(),cryptoCurrency
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