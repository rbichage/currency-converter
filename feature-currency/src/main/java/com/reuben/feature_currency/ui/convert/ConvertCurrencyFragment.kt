package com.reuben.feature_currency.ui.convert

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.reuben.core_common.CurrencyType
import com.reuben.core_common.date.fragment.viewBinding
import com.reuben.core_model.api.TimeSeries
import com.reuben.core_model.currency.CurrenciesList
import com.reuben.core_model.currency.Currency
import com.reuben.core_model.currency.ExchangeRate
import com.reuben.core_navigation.NavigationDirections
import com.reuben.feature_currency.R
import com.reuben.feature_currency.databinding.FragmentConvertCurrencyBinding
import com.reuben.feature_currency.ui.CurrencyUIState
import com.reuben.feature_currency.ui.CurrencyViewModel
import com.reuben.feature_currency.util.SELECTED_CURRENCY_REQUEST_KEY
import com.reuben.feature_currency.util.SELECTED_CURRENCY_TYPE_BUNDLE_KEY
import com.reuben.feature_currency.util.SELECTED_CURRENCY_VALUE_BUNDLE_KEY
import com.reuben.feature_currency.util.text.DestinationAmountTextWatcher
import com.reuben.feature_currency.util.text.SourceAmountTextWatcher
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject


@AndroidEntryPoint
class ConvertCurrencyFragment : Fragment(R.layout.fragment_convert_currency) {

    private val binding by viewBinding(FragmentConvertCurrencyBinding::bind)

    private val viewModel by viewModels<CurrencyViewModel>()

    @Inject
    lateinit var navigator: NavigationDirections

    private var baseCurrency = ""
    private var destCurrency = ""

    private var sourceAmountTextWatcher: SourceAmountTextWatcher? = null
    private var destAmountTextWatcher: DestinationAmountTextWatcher? = null

    private val exchangeRates = mutableSetOf<ExchangeRate>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRefresh()
        setupObservers()
    }

    private fun setupRefresh() {
        binding.root.setOnRefreshListener {
            viewModel.getCurrencies()
        }
    }

    override fun onResume() {
        super.onResume()
        if (baseCurrency.isEmpty() || destCurrency.isEmpty()){
            return
        }

        if (baseCurrency.contains(destCurrency, true)){
            return
        }

        viewModel.getExchangeRatesForCurrency(baseCurrency)
    }

    override fun onStop() {
        super.onStop()
        viewModel.resetUiState()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    Timber.e("ui state $uiState")
                    when (uiState) {
                        is CurrencyUIState.Currencies -> {
                            isLoading(false)
                            setupCurrencies(uiState.data)
                        }

                        is CurrencyUIState.Error -> {
                            isLoading(false)
                            Toast.makeText(requireContext(), uiState.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is CurrencyUIState.ExchangeRateHistory -> {
                            navigateToCurrencyDetails(uiState.data)
                        }

                        is CurrencyUIState.ExchangeRates -> {
                            isLoading(false)
                            exchangeRates.addAll(uiState.data)
                            setupExchangeRate(uiState.data)
                        }

                        CurrencyUIState.Idle -> {
                            isLoading(false)
                            binding.root.isRefreshing = false
                        }

                        CurrencyUIState.Loading -> {
                            isLoading(true)
                        }
                    }
                }
            }
        }

        viewModel.getCurrencies()
    }

    private fun navigateToCurrencyDetails(timeSeries: List<TimeSeries>) {
        navigator.navigateToCurrencyDetails(
            navController = findNavController(),
            currenciesList = exchangeRates.toTypedArray(),
            originCurrency = baseCurrency,
            destinationCurrency = destCurrency,
            timeSeries = timeSeries.toTypedArray()
        )
    }

    private fun setupExchangeRate(data: List<ExchangeRate>) {
        binding.btnDetails.isEnabled = true
        data.firstOrNull {
            it.currencyCode.contains(destCurrency)
        }?.let { exchangeRate ->
            with(binding) {
                etSourceAmount.setText("1")
                etDestinationAmount.setText(DecimalFormat("#.##").format((1 * exchangeRate.currencyValue)))

                setAmountFocusChangedListener(exchangeRate)
            }
        } ?: Timber.e("currency for $destCurrency not found")

        binding.btnDetails.setOnClickListener {
            viewModel.getExchangeRateHistory(baseCurrency, destCurrency)
        }

    }

    private fun setAmountFocusChangedListener(exchangeRate: ExchangeRate) {

        with(binding.etSourceAmount) {
            setOnFocusChangeListener { _, isFocused ->
                if (isFocused) {

                    sourceAmountTextWatcher = SourceAmountTextWatcher { text ->
                        val convertedValue =
                            DecimalFormat("#.##").format((text.toDouble() * exchangeRate.currencyValue))
                        binding.etDestinationAmount.setText(convertedValue)
                    }
                    addTextChangedListener(sourceAmountTextWatcher)

                } else {
                    sourceAmountTextWatcher?.let { removeTextChangedListener(it) }
                }
            }
        }

        with(binding.etDestinationAmount) {
            setOnFocusChangeListener { _, isFocused ->
                if (isFocused) {

                    destAmountTextWatcher = DestinationAmountTextWatcher { text ->
                        val convertedValue = DecimalFormat("#.##").format(
                            (text.toDouble().div(exchangeRate.currencyValue))
                        )
                        binding.etSourceAmount.setText(convertedValue)
                    }
                    addTextChangedListener(destAmountTextWatcher)
                } else {
                    destAmountTextWatcher?.let {
                        removeTextChangedListener(it)
                    }
                }
            }
        }


        binding.imgSwitchCurrency.setOnClickListener {
            switchCurrency(baseCurrency, destCurrency)
        }
    }

    private fun switchCurrency(origin: String, dest: String) {
        baseCurrency = dest
        destCurrency = origin
        val originalSourceCurrency = binding.etSourceCurrency.text.toString()
        with(binding) {
            etSourceCurrency.setText(binding.etDestCurrency.text.toString())
            etDestCurrency.setText(originalSourceCurrency)
        }
        val currency = Currency(baseCurrency, binding.etSourceCurrency.text.toString())
        updateCurrency(currencyType = CurrencyType.SOURCE, currency)
    }

    private fun isLoading(show: Boolean) {

        with(binding) {
            etDestCurrency.isEnabled = !show
            etSourceCurrency.isEnabled = !show
            etSourceAmount.isEnabled = !show
            etDestinationAmount.isEnabled = !show
            imgSwitchCurrency.isEnabled = !show
            btnDetails.isVisible = !show
            root.isRefreshing = show
        }


    }

    private fun setupCurrencies(data: CurrenciesList) {
        setupResults()
        with(binding) {
            etDestCurrency.setOnClickListener {
                navigator.navigateToCurrencies(
                    findNavController(), data, CurrencyType.DEST
                )
            }

            etSourceCurrency.setOnClickListener {
                navigator.navigateToCurrencies(
                    findNavController(), data, CurrencyType.SOURCE
                )
            }
        }
    }

    private fun setupResults() {
        setFragmentResultListener(SELECTED_CURRENCY_REQUEST_KEY) { _, bundle ->
            val currencyType =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bundle.getSerializable(
                        SELECTED_CURRENCY_TYPE_BUNDLE_KEY,
                        CurrencyType::class.java
                    )
                } else {
                    @Suppress("DEPRECATION") bundle.getSerializable(
                        SELECTED_CURRENCY_TYPE_BUNDLE_KEY
                    ) as CurrencyType
                }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                bundle.getParcelable(SELECTED_CURRENCY_VALUE_BUNDLE_KEY, Currency::class.java)
            } else {
                @Suppress("DEPRECATION") bundle.getParcelable(SELECTED_CURRENCY_VALUE_BUNDLE_KEY)
            }?.let { currency ->
                currencyType?.let { updateCurrency(currencyType, currency) }
            }
        }
    }

    private fun updateCurrency(currencyType: CurrencyType, currency: Currency) {
        when (currencyType) {
            CurrencyType.SOURCE -> {
                baseCurrency = currency.currencyCode
                binding.etSourceCurrency.setText(currency.currencyName)
                if (destCurrency.isNotEmpty() && !baseCurrency.contains(destCurrency, true)) {
                    viewModel.getExchangeRatesForCurrency(currency.currencyCode)
                }
            }

            CurrencyType.DEST -> {
                destCurrency = currency.currencyCode
                binding.etDestCurrency.setText(currency.currencyName)
                if (baseCurrency.isNotEmpty() && !baseCurrency.contains(destCurrency, true)) {
                    viewModel.getExchangeRatesForCurrency(baseCurrency)
                }
            }
        }
    }

}

