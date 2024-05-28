package com.example.cfinanceapp.tools


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.api.CoinMarketCapAPI
import com.example.cfinanceapp.data.Repository
import com.example.cfinanceapp.data.local.DatabaseInstance
import com.example.cfinanceapp.data.models.Account
import com.example.cfinanceapp.data.models.Asset
import com.example.cfinanceapp.data.models.CryptoCurrency
import com.example.cfinanceapp.data.models.Favorite
import com.example.cfinanceapp.data.models.Transaction
import com.example.cfinanceapp.data.models.Wallet
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random


class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = Repository(CoinMarketCapAPI, DatabaseInstance.getDatabase(application))


    private val firebaseAuthentication = Firebase.auth


    var cryptoList = repository.coinsList
    val accounts = repository.accounts
    val favorites = repository.favorites


    private val _maxAmount = MutableLiveData<String>()
    val maxAmount: LiveData<String>
        get() = _maxAmount

    private var _currentFavorites = MutableLiveData<MutableList<Favorite>>()
    val currentFavorites: LiveData<MutableList<Favorite>>
        get() = _currentFavorites

    private var _currentTransactions = MutableLiveData<List<Transaction>>()
    val currentTransactions: LiveData<List<Transaction>>
        get() = _currentTransactions

    private var _currentTransaction = MutableLiveData<Transaction>()
    val currentTransaction: LiveData<Transaction>
        get() = _currentTransaction

    private var _currentAssets = MutableLiveData<MutableList<Asset>>()
    val currentAssets: LiveData<MutableList<Asset>>
        get() = _currentAssets

    private var _currentAccount = MutableLiveData<Account>()
    val currentAccount: LiveData<Account>
        get() = _currentAccount

    private var _currentWallet = MutableLiveData<Wallet?>()
    val currentWallet: LiveData<Wallet?>
        get() = _currentWallet

    private var _currentCrypto = MutableLiveData<CryptoCurrency>()
    val currentCrypto: LiveData<CryptoCurrency>
        get() = _currentCrypto


    init {
        loadCrypto()
        loadAllAccounts()
        loadAllWallets()
        loadAllAssets()
        loadAllTransactions()
        loadAllFavorites()
    }

    private fun loadAllFavorites() {
        viewModelScope.launch {
            repository.getAllFavorites()
        }
    }

    private fun loadAllTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions()
        }
    }

    private fun loadAllAssets() {
        viewModelScope.launch {
            repository.getAllAssets()
        }
    }

    private fun loadAllWallets() {
        viewModelScope.launch {
            repository.getAllWallets()
        }
    }

    private fun loadAllAccounts() {
        viewModelScope.launch {
            repository.getAllAccounts()
        }
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()

        }
    }

    /**
     * This function sorts the first 10 crypto coins in our market list based on the 24h volume by descending.
     */
    fun loadHotList(): List<CryptoCurrency> {
        val sortedByVolume = cryptoList.value!!.data
        return sortedByVolume.subList(0, 10).sortedByDescending { it.quote.usdData.volume24h }

    }

    fun getCurrentCoin(coin: CryptoCurrency) {
        viewModelScope.launch {
            _currentCrypto.value = coin
        }
    }

    fun getCurrentTransaction(transaction: Transaction) {
        viewModelScope.launch {
            _currentTransaction.value = transaction
        }
    }

    private fun createNewAccount(account: Account) {
        viewModelScope.launch {
            repository.insertAccount(account)
        }
    }

    fun createNewWallet() {
        val wallet = Wallet(accountId = _currentAccount.value!!.id)
        viewModelScope.launch {
            repository.insertWallet(wallet)
            _currentWallet.value = wallet
        }
    }

    private fun insertAsset(asset: Asset) {
        viewModelScope.launch {
            repository.insertAssets(asset)
        }
    }

    private fun updateAsset(asset: Asset) {
        viewModelScope.launch {
            repository.updateAssets(asset)
        }
    }

    private fun updateAccount(account: Account) {
        viewModelScope.launch {
            repository.updateAccount(account)
        }
    }

    private fun removeCryptoCurrencyAsset(asset: Asset) {
        viewModelScope.launch {
            repository.removeAsset(asset)
        }
    }

    private fun insertTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransactions(transaction)
        }
    }

    private fun insertFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.insertFavorite(favorite)
        }
    }

    private fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.removeFromFavorite(favorite)
        }
    }

    fun findWalletByUserId() {
        if (_currentAccount.value != null) {
            viewModelScope.launch {
                _currentWallet.value = repository.getWalletById(_currentAccount.value!!.id)
            }
        }

    }

    fun findAssetsByWalletId() {
        if (_currentWallet.value != null) {
            viewModelScope.launch {
                _currentAssets.value = repository.getAssetsByWalletId(_currentWallet.value!!.id)
            }
        }

    }

    fun findTransactionsByWalletId() {
        if (_currentWallet.value != null) {
            viewModelScope.launch {
                _currentTransactions.value =
                    repository.getTransactionsByWalletId(_currentWallet.value!!.id)
            }
        }
    }

    fun findFavoritesByAccountId(accountId: Long) {
        viewModelScope.launch {
            _currentFavorites.value = repository.getFavoritesByAccountId(accountId)
        }
    }

    /**
     * This function is to buy crypto currency and add it to our assets.
     * If the coin exist then it updates the amount else it adds it as new in our database.
     * @param amount is the wished amount we want to buy .
     * @param coin is the crypto currency we want to buy.
     */
    @SuppressLint("NewApi")
    fun updateOrInsertCryptoCurrencyAmounts(amount: Double, coin: CryptoCurrency) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val transaction = Transaction(
            symbol = coin.symbol,
            amount = amount,
            price = coin.quote.usdData.price,
            date = formattedDateTime,
            transactionHash = generateTransactionHash(),
            isBought = true,
            walletId = _currentWallet.value!!.id
        )

        viewModelScope.launch {
            val assets = repository.getAssetsByWalletId(_currentWallet.value!!.id)
            val existedAsset = assets.find { it.cryptoCurrency?.symbol == coin.symbol }
            val fiatAsset = assets.find { it.cryptoCurrency == null }
            if (existedAsset != null) {
                val updatedFiatAmount = fiatAsset?.amount!! - (coin.quote.usdData.price * amount)
                fiatAsset.amount = updatedFiatAmount
                val updatedAmount = existedAsset.amount.plus(amount)
                existedAsset.amount = updatedAmount
                updateAsset(fiatAsset)
                updateAsset(existedAsset)
                insertTransaction(transaction)
            } else {
                val updatedFiatAmount = fiatAsset?.amount!! - (coin.quote.usdData.price * amount)
                fiatAsset.amount = updatedFiatAmount
                val newAsset = Asset(
                    fiat = (coin.quote.usdData.price * amount).toString(),
                    cryptoCurrency = coin,
                    amount = amount,
                    walletId = _currentWallet.value!!.id
                )
                updateAsset(fiatAsset)
                insertAsset(newAsset)
                insertTransaction(transaction)
            }
        }

    }

    /**
     * This function is to deposit our virtual fiat assets in our wallet.
     * If already existed in database it updates the amount , if not then it adds it to the database.
     * @param amount is for the amount we want to put in our database.
     */
    @SuppressLint("NewApi")
    fun updateOrInsertFiatCurrencyAmounts(amount: Double) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val transaction = Transaction(
            symbol = "USD",
            amount = amount,
            price = null,
            date = formattedDateTime,
            transactionHash = "Fiat Deposit",
            isBought = null,
            walletId = _currentWallet.value!!.id
        )
        viewModelScope.launch {
            val assets = repository.getAssetsByWalletId(_currentWallet.value!!.id)
            val existedFiatAsset = assets.find { it.fiat != null }
            if (existedFiatAsset != null) {
                val updatedAmount = existedFiatAsset.amount.plus(amount)
                existedFiatAsset.amount = updatedAmount
                updateAsset(existedFiatAsset)
                insertTransaction(transaction)
            } else {
                val newFiatAsset = Asset(
                    fiat = "USD",
                    cryptoCurrency = null,
                    amount = amount,
                    walletId = _currentWallet.value!!.id
                )
                insertAsset(newFiatAsset)
                insertTransaction(transaction)
            }
        }
    }

    /**
     * This function is to sell the current crypto we have in our assets.
     * If the amount is 0 the crypto is removed from our database else we update the amount.
     * @param coin is for the crypto we want to sell.
     * @param amount is for the amount of the crypto asset we want to sell.
     */
    @SuppressLint("NewApi")
    fun sellCryptoCurrencyAsset(coin: CryptoCurrency, amount: Double) {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val formattedDateTime = currentDateTime.format(formatter)
        val transaction = Transaction(
            symbol = coin.symbol,
            amount = amount,
            price = coin.quote.usdData.price,
            date = formattedDateTime,
            transactionHash = generateTransactionHash(),
            isBought = false,
            walletId = _currentWallet.value!!.id
        )
        viewModelScope.launch {
            val assets = repository.getAssetsByWalletId(_currentWallet.value!!.id)
            val existedAsset = assets.find { it.cryptoCurrency?.symbol == coin.symbol }
            val fiatAsset = assets.find { it.cryptoCurrency == null }
            if (existedAsset != null && isEnoughCrypto(amount)) {
                val updatedFiatAmount = fiatAsset?.amount!! + (coin.quote.usdData.price * amount)
                fiatAsset.amount = updatedFiatAmount
                val updatedAmount = existedAsset.amount.minus(amount)
                existedAsset.amount = updatedAmount
                updateAsset(fiatAsset)
                updateAsset(existedAsset)
                insertTransaction(transaction)
                if (existedAsset.amount == 0.0) {
                    removeCryptoCurrencyAsset(existedAsset)
                }
            }


        }

    }

    /**
     * This function is to check if the account we want to register or login is saved on our database.
     * @param email is the account email to make the check.
     */
    fun isAccountAlreadyRegistered(email: String): Boolean {
        return accounts.value?.any { it.email == email } ?: false
    }

    fun findAccountByEmail(email: String) {
        viewModelScope.launch {
            _currentAccount.value = repository.getAccountByEmail(email)
        }
    }

    /**
     * This function gets a coin logo from our API for every specific coin from its id.
     * @param id is for the coin id.
     */
    fun getCoinLogo(id: String): String {
        return "https://s2.coinmarketcap.com/static/img/coins/64x64/$id.png"
    }

    /**
     * This function separates our search crypto in market fragment in a temporary mutable list
     * to show on our recycle view.
     * @param query is for the text we give in our search view.
     */
    fun search(query: String): MutableList<CryptoCurrency> {
        val searchList = mutableListOf<CryptoCurrency>()
        for (coin in cryptoList.value!!.data) {
            if (coin.name.lowercase().contains(query) || coin.symbol.lowercase().contains(query))
                searchList.add(coin)
        }
        return searchList
    }

    /**
     * This function filters the list to Gainers in percentage and Losers in percentage for our market fragment.
     * @param input is the choice we click in our "spinner".
     */
    fun filteredCryptoLists(input: String): List<CryptoCurrency> {
        val gainers = cryptoList.value!!.data.filter { it.quote.usdData.percentChange24h > 0 }
        val losers = cryptoList.value!!.data.filter { it.quote.usdData.percentChange24h < 0 }

        return when (input) {
            "Gain" -> gainers.sortedByDescending { it.quote.usdData.percentChange24h }
            "Lose" -> losers.sortedBy { it.quote.usdData.percentChange24h }
            "All" -> cryptoList.value!!.data
            else -> cryptoList.value!!.data
        }
    }

    /**
     * This function is to filter the transactions is Bought , Sold or Deposits
     * all sorted by date.
     * @param input is the actual choice we click in our "spinner".
     */
    fun filteredTransactionsList(input: String): List<Transaction> {
        val boughtTransactions = _currentTransactions.value?.filter { it.isBought == true }!!
            .sortedByDescending { it.date }
        val soldTransactions = _currentTransactions.value?.filter { it.isBought == false }!!
            .sortedByDescending { it.date }
        val depositTransactions = _currentTransactions.value?.filter { it.symbol == "USD" }!!
            .sortedByDescending { it.date }
        val allTransactionsSortedByDate = _currentTransactions.value?.sortedByDescending { it.date }

        return when (input) {
            "Bought" -> boughtTransactions
            "Sold" -> soldTransactions
            "Deposits" -> depositTransactions
            "All" -> allTransactionsSortedByDate!!
            else -> allTransactionsSortedByDate!!

        }
    }

    /**
     * This function adds to our database a watchlist element.
     * @param coin is the crypto we want to add in our watchlist.
     */
    fun addToWatchlist(coin: CryptoCurrency) {
        viewModelScope.launch {
            val favorites = repository.getFavoritesByAccountId(_currentAccount.value!!.id)
            val existedFavorite = favorites.find { it.favoriteCoin!!.id == coin.id }
            if (existedFavorite != null) {
                removeFavorite(existedFavorite)
            } else {
                val newFavorite = Favorite(
                    favoriteCoin = coin,
                    accountId = _currentAccount.value!!.id
                )
                insertFavorite(newFavorite)
            }
        }

    }

    /**
     *This function shows a small spark like a chart in our watchlist elements.
     * Just for the visual sensation.
     * @param coinId is to set the coin id from the actual coin so it shows the chart direction of the coin in this sparky line.
     */
    fun getChartEffect(coinId: String): String {
        return "https://s3.coinmarketcap.com/generated/sparklines/web/1d/usd/$coinId.png"
    }

    /**
     * This function generates a "transaction hash" for our crypto currency transaction just to give
     * the sensation of reality.
     */
    private fun generateTransactionHash(): String {
        val hexChars = "0123456789abcdef"
        val sb = StringBuilder(32)
        repeat(32) {
            val randomIndex = Random.nextInt(0, hexChars.length)
            sb.append(hexChars[randomIndex])
        }
        return sb.toString()
    }

    /**
     * This function registers and creates a new account in our Google firebase .
     * @param email is for the user email.
     * @param password is for the user password.
     * @param completion is a unit function to determine what is going to happen after the completion of the authentication.
     */
    fun registration(email: String, password: String, name: String, completion: () -> Unit) {
        val account = Account(email = email, name = name)
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        createNewAccount(account)
                        completion()
                    } else {
                        Log.e("FIREBASE", it.exception.toString())
                        Log.e("FIREBASE", "email: $email")
                        Log.e("FIREBASE", "password : $password")

                    }
                }
        }
    }

    /**
     * This function is a login authentication from Google firebase.
     * @param email is for the user email.
     * @param password is for the user password.
     * @param completion is a unit function to determine what is going to happen after the completion of the authentication.
     */
    fun loginAuthentication(email: String, password: String, completion: () -> Unit) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            firebaseAuthentication.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        completion()
                    } else {
                        Log.e("FIREBASE", it.exception.toString())
                        Log.e("FIREBASE", "email: $email")
                        Log.e("FIREBASE", "password : $password")

                    }
                }
        }
    }

    fun logout() {
        firebaseAuthentication.signOut()
    }


    /**
     * This function counts our current balance in a summary of our assets , fiat , profit and loss .
     */
    fun currentBalance(): Double {
        var balance = 0.0
        if (_currentAssets.value != null) {
            for (asset in _currentAssets.value!!) {
                val assetValue: Double = actualCoinPriceUpdater(asset)
                balance += (assetValue * asset.amount)
            }
            val fiatAsset =
                _currentAssets.value?.find { it.cryptoCurrency == null }?.amount ?: 0.0
            balance += fiatAsset
        } else {
            balance = 0.0
        }
        return balance
    }

    /**
     * This function counts the actual profit or loss value in our balance in fiat(USD $).
     */
    fun profitOrLoss(): Double {
        var actualInvestment = 0.0
        if (_currentTransactions.value != null) {
            val fiatTransactions = _currentTransactions.value!!.filter { it.price == null }
            actualInvestment = fiatTransactions.sumOf { it.amount }
        }
        val initialInvestment = actualInvestment
        return currentBalance() - initialInvestment
    }

    /**
     * This function counts the percentage of profit or loss in our balance from our transactions.
     */
    fun profitOrLossPercentage(): Double {
        var actualInvestment = 0.0
        if (_currentTransactions.value != null) {
            val fiatTransactions = _currentTransactions.value!!.filter { it.price == null }
            actualInvestment = fiatTransactions.sumOf { it.amount }
        }
        return (profitOrLoss() / actualInvestment) * 100.0
    }

    /**
     * This function checks if is our balance enough to buy the crypto currency we looking at.
     * @param amount is the amount we look to buy.
     */
    fun isEnoughFiat(amount: Double): Boolean {
        val fiatBalance = _currentAssets.value?.find { it.fiat == "USD" }
        val wishAmount = _currentCrypto.value?.quote?.usdData!!.price * amount

        return fiatBalance?.fiat != null && fiatBalance.amount >= wishAmount
    }

    /**
     * This function checks if there is enough crypto to sell or we are trying to sell more
     * than that we have.
     * @param amount is the amount of crypto currency we have in our assets.
     */
    fun isEnoughCrypto(amount: Double): Boolean {
        val cryptoBalance = _currentAssets.value?.find { it.cryptoCurrency != null }
        return cryptoBalance?.cryptoCurrency != null && cryptoBalance.amount >= amount
    }

    /**
     * This function checks if the actual coin we looking at is on the favorites watchlist.
     * @param coin is for the actual coin we looking at.
     */
    fun isFavorite(coin: CryptoCurrency): Boolean {
        val favoriteCoins = _currentFavorites.value
        val currentCryptoId = coin.id
        return favoriteCoins?.any { it.favoriteCoin?.id == currentCryptoId } ?: false
    }

    /**
     * This function updates the price in a coroutine from the saved asset in our database
     * to the actual coin price.
     * @param asset is for the asset element we have in our database.
     */
    fun actualCoinPriceUpdater(asset: Asset): Double {
        var actualCoinPrice = 0.0
        viewModelScope.launch {
            actualCoinPrice = if (_currentAssets.value != null) {
                val actualCryptoPrice =
                    cryptoList.value?.data?.find { it.id == asset.cryptoCurrency?.id }?.quote?.usdData?.price
                        ?: 0.0
                actualCryptoPrice
            } else {
                0.0
            }
        }
        return actualCoinPrice
    }

    /**
     * This function does the same as actualCoinFinderWatchlist but this time for the assets
     * recycler view.
     * @param asset is for the asset element in our recycler view.
     */
    fun actualCoinFinder(asset: Asset): CryptoCurrency? {
        viewModelScope.launch {
            val actualCrypto = cryptoList.value?.data?.find { it.id == asset.cryptoCurrency?.id }
            asset.cryptoCurrency = actualCrypto
        }
        return asset.cryptoCurrency
    }

    fun actualCoinPriceUpdaterWatchlist(favorite: Favorite): Double {
        var actualCoinPrice = 0.0
        viewModelScope.launch {
            actualCoinPrice = if (_currentFavorites.value != null) {
                val actualCryptoPrice =
                    cryptoList.value?.data?.find { it.id == favorite.favoriteCoin?.id }?.quote?.usdData?.price
                        ?: 0.0
                actualCryptoPrice
            } else {
                0.0
            }
        }
        return actualCoinPrice
    }

    /**
     * This function finds the asset coin inside a coroutine and give it the actual
     * data of this coin from the API so when we navigate from our watchlist to this specific coin
     * we get the actual measurements so we are not outdated.
     * @param favorite is for the favorite element in our recycler view.
     */
    fun actualCoinFinderWatchlist(favorite: Favorite): CryptoCurrency? {
        viewModelScope.launch {
            val actualCrypto = cryptoList.value?.data?.find { it.id == favorite.favoriteCoin?.id }
            favorite.favoriteCoin = actualCrypto
        }
        return favorite.favoriteCoin
    }

    /**
     * This function finds the difference from the price we bought the coin and the actual coin price
     * so we can se if we have profited from this trade or not.
     * @param asset is the asset we are looking for to make the comparison.
     */
    fun profitOrLossInAsset(asset: Asset): Double {
        val actualCoinPrice =
            cryptoList.value?.data?.find { it.id == asset.cryptoCurrency?.id }?.quote?.usdData?.price
        val assetSavedPrice = asset.cryptoCurrency?.quote?.usdData?.price ?: 0.0
        return actualCoinPrice?.minus(assetSavedPrice) ?: 0.0
    }


    /**
     * This function finds the specific coin we look to sell and give us the
     * maximum available amount that we have in our wallet to sell.
     * @param coin is the specific coin we are at to sell.
     */
    fun maxOfAsset(coin: CryptoCurrency) {
        viewModelScope.launch {
            val assets = repository.getAssetsByWalletId(_currentWallet.value!!.id)
            val existedAsset = assets.find { it.cryptoCurrency?.symbol == coin.symbol }
            _maxAmount.postValue((existedAsset?.amount ?: 0.0).toString())
        }
    }

    /**
     * This function finds the fiat assets (USD $) in our assets and counts
     * how many coins can we get from the crypto currency that we choose to buy
     * according to the fiat(USD $) we have.
     * @param coin is the coin we are at to buy.
     */
    fun maxToBuy(coin: CryptoCurrency) {
        viewModelScope.launch {
            val assets = repository.getAssetsByWalletId(_currentWallet.value!!.id)
            val existedFiatAsset = assets.find { it.fiat == "USD" }
            val fiatAmount = existedFiatAsset?.amount ?: 0.0
            val cryptoPrice = coin.quote.usdData.price

            val maxBuyAmount = if (existedFiatAsset != null) {
                fiatAmount / cryptoPrice
            } else {
                0.0
            }
            _maxAmount.postValue("${String.format("%.5f", maxBuyAmount)} ")
        }
    }

    /**
     * This function reset the amount viewed in the transaction view after we click
     * the button to show the bottom sheet .
     */
    fun resetMaxAmount() {
        _maxAmount.value = ""
    }

    /**
     * This function works together with the formatDecimalsAmount function.
     * finds the index of the last zero before the first non-zero number.
     * @param number is double number we need to find the index.
     */
    private fun getDecimalPlaces(number: Double): Int {

        val numberSubstring = number.toString().substringAfter(".")

        for ((index, char) in numberSubstring.withIndex()) {
            if (char != '0') {
                return index + 1
            }
        }
        return 0
    }

    /**
     * This function forms decimals after the comma of a double number
     * and finds how many zeros are there , counting that,sets the view of how many decimals
     * we want to be visible (experimental and buggy).
     * @param amount is the parameter we need to count.
     */
    fun formatDecimalsAmount(amount: Double): String {

        val significantDecimals = getDecimalPlaces(amount)

        return if (significantDecimals > 0) {
            String.format("%.${significantDecimals}f $", amount)
        } else {
            String.format("%.3f $", amount)
        }
    }

    /**
     * This function updates the user name that is logged in the application.
     * @param name is the parameter for the updated user name.
     */
    fun updateUserName(name: String) {
        val updatedAccount = Account(
            id = _currentAccount.value!!.id,
            email = _currentAccount.value!!.email,
            name = name
        )
        updateAccount(updatedAccount)
    }


}