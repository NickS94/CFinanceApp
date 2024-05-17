package com.example.cfinanceapp.tools


import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cfinanceapp.data.API.CoinMarketCapAPI
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
    val wallets = repository.wallets
    val assets = repository.assets
    val favorites = repository.favorites
    val transactions = repository.transactions


    private var _currentFavorites = MutableLiveData<MutableList<Favorite>>()
    val currentFavorites: LiveData<MutableList<Favorite>> = _currentFavorites

    private var _currentTransactions = MutableLiveData<List<Transaction>>()
    val currentTransactions: LiveData<List<Transaction>> = _currentTransactions

    private var _currentTransaction = MutableLiveData<Transaction>()
    val currentTransaction: LiveData<Transaction> = _currentTransaction

    private var _currentAssets = MutableLiveData<MutableList<Asset>>()
    val currentAssets: LiveData<MutableList<Asset>> = _currentAssets

    private var _currentAccount = MutableLiveData<Account>()
    val currentAccount: LiveData<Account> = _currentAccount

    private var _currentWallet = MutableLiveData<Wallet?>()
    val currentWallet: LiveData<Wallet?> = _currentWallet

    private var _currentCrypto = MutableLiveData<CryptoCurrency>()
    val currentCrypto: LiveData<CryptoCurrency> = _currentCrypto


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
    fun loadAllTransactions() {
        viewModelScope.launch {
            repository.getAllTransactions()
        }
    }

    fun loadAllAssets() {
        viewModelScope.launch {
            repository.getAllAssets()
        }
    }

    fun loadAllWallets() {
        viewModelScope.launch {
            repository.getAllWallets()
        }
    }

    fun loadAllAccounts() {
        viewModelScope.launch {
            repository.getAllAccounts()
        }
    }

    private fun loadCrypto() {
        viewModelScope.launch {
            repository.loadCryptoCurrencyList()

        }
    }

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

    fun findWalletByUserId(accountId: Long) {
        viewModelScope.launch {
            _currentWallet.value = repository.getWalletById(accountId)
        }
    }

    fun findAssetsByWalletId(walletId: Long) {
        viewModelScope.launch {
            _currentAssets.value = repository.getAssetsByWalletId(walletId)
        }
    }

    fun findTransactionsByWalletId(walletId: Long) {
        viewModelScope.launch {
            _currentTransactions.value = repository.getTransactionsByWalletId(walletId)
        }
    }

    fun findFavoritesByAccountId(accountId: Long) {
        viewModelScope.launch {
            _currentFavorites.value = repository.getFavoritesByAccountId(accountId)
        }
    }

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
        val existedFiatAsset = _currentAssets.value?.find { it.fiat != null }
        if (existedFiatAsset != null && _currentAssets.value != null) {
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

    fun isAccountAlreadyRegistered(email: String): Boolean {
        return accounts.value?.any { it.email == email } ?: false
    }

    fun findAccountByEmail(email: String) {
        viewModelScope.launch {
            _currentAccount.value = repository.getAccountByEmail(email)
        }
    }

    fun getCoinLogo(id: String): String {
        return "https://s2.coinmarketcap.com/static/img/coins/64x64/$id.png"
    }

    fun search(query: String): MutableList<CryptoCurrency> {
        val searchList = mutableListOf<CryptoCurrency>()
        for (coin in cryptoList.value!!.data) {
            if (coin.name.lowercase().contains(query) || coin.symbol.lowercase().contains(query))
                searchList.add(coin)
        }
        return searchList
    }

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

    fun filteredTransactionsList(input: String): List<Transaction> {
        val boughtTransactions = _currentTransactions.value?.filter { it.isBought == true }!!.sortedByDescending { it.date }
        val soldTransactions = _currentTransactions.value?.filter { it.isBought == false }!!.sortedByDescending { it.date }
        val depositTransactions = _currentTransactions.value?.filter { it.symbol == "USD" }!!.sortedByDescending { it.date }
        val allTransactionsSortedByDate = _currentTransactions.value?.sortedByDescending { it.date }

        return when (input) {
            "Bought" -> boughtTransactions
            "Sold" -> soldTransactions
            "Deposits" -> depositTransactions
            "All" -> allTransactionsSortedByDate!!
            else -> allTransactionsSortedByDate!!

        }
    }

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

    fun getChartEffect(coinId: String): String {
        return "https://s3.coinmarketcap.com/generated/sparklines/web/1d/usd/$coinId.png"
    }

    private fun generateTransactionHash(): String {
        val hexChars = "0123456789abcdef"
        val sb = StringBuilder(32)
        repeat(32) {
            val randomIndex = Random.nextInt(0, hexChars.length)
            sb.append(hexChars[randomIndex])
        }
        return sb.toString()
    }

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

    fun profitOrLoss(): Double {
        var actualInvestment = 0.0
        if (_currentTransactions.value != null) {
            val fiatTransactions = _currentTransactions.value!!.filter { it.price == null }
            actualInvestment = fiatTransactions.sumOf { it.amount }
        }
        val initialInvestment = actualInvestment
        val currentBalance = currentBalance()
        return currentBalance - initialInvestment
    }

    fun isEnoughFiat(amount: Double): Boolean {
        val fiatBalance = _currentAssets.value?.find { it.fiat == "USD" }
        val wishAmount = _currentCrypto.value?.quote?.usdData!!.price * amount
        return fiatBalance?.fiat != null && fiatBalance.amount >= wishAmount
    }

    fun isEnoughCrypto(amount: Double): Boolean {
        val cryptoBalance = _currentAssets.value?.find { it.cryptoCurrency != null }
        return cryptoBalance?.cryptoCurrency != null && cryptoBalance.amount >= amount
    }

    fun isFavorite(coin: CryptoCurrency): Boolean {
        val favoriteCoins = _currentFavorites.value
        val currentCryptoId = coin.id
        return favoriteCoins?.any { it.favoriteCoin?.id == currentCryptoId } ?: false
    }

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

    fun actualCoinFinderWatchlist(favorite: Favorite): CryptoCurrency? {
        viewModelScope.launch {
            val actualCrypto = cryptoList.value?.data?.find { it.id == favorite.favoriteCoin?.id }
            favorite.favoriteCoin = actualCrypto
        }
        return favorite.favoriteCoin
    }

    fun profitOrLossInAsset(asset: Asset): Double {
        val actualCoinPrice =
            cryptoList.value?.data?.find { it.id == asset.cryptoCurrency?.id }?.quote?.usdData?.price
        val assetSavedPrice = asset.cryptoCurrency?.quote?.usdData?.price ?: 0.0
        return actualCoinPrice?.minus(assetSavedPrice) ?: 0.0
    }
}