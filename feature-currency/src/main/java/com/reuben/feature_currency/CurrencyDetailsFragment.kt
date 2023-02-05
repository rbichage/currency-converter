package com.reuben.feature_currency

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.reuben.core_common.date.fragment.viewBinding
import com.reuben.feature_currency.databinding.FragmentCurrencyDetailsBinding

class CurrencyDetailsFragment : Fragment(R.layout.fragment_currency_details) {
    private val binding by viewBinding(FragmentCurrencyDetailsBinding::bind)

    private val args by navArgs<CurrencyDetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavigation()
        setupChart()
        setupPopularCurrencies()
    }

    private fun setupPopularCurrencies() {
        val exchangeRates = args.exchangeRates.toList()

        mostTradedCurrencies.removeIf {
            it.first.contains(args.baseCurrencyCode)
        }

        val common = mostTradedCurrencies.map { currency ->
            val rate = exchangeRates.first { it.currencyCode.contains(currency.first, true) }
            Pair(currency.second, rate)
        }

        val currencySpan = SpannableStringBuilder()
        val valueSpan = SpannableStringBuilder()

        common.forEach {
            currencySpan.apply {
                append(it.first)
                append("\n")
                append("\n")
            }

            valueSpan.apply {
                append(it.second.currencyValue.toString())
                append("\n")
                append("\n")
            }
        }

        binding.textCurrencyName.text = currencySpan
        binding.textCurrencyRate.text = valueSpan

    }

    private fun setupChart() {
        val timeSeries = args.timeseries
        val xValues = (0..args.timeseries.size).toList()

        val chartData = args.timeseries.map {
            val position = args.timeseries.indexOf(it)
            Entry(xValues[position].toFloat(), it.exchangeRate.toFloat())
        }

        val xValueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                val index = value.toInt()

                return if (index < timeSeries.size) {
                    timeSeries[index].date
                } else ""
            }
        }

        val lineDataSet =
            LineDataSet(chartData, "${args.baseCurrencyCode}-${args.destCurrencyCode}").apply {
                mode = LineDataSet.Mode.HORIZONTAL_BEZIER
                color = Color.GREEN
                setDrawFilled(true)
                fillColor = ContextCompat.getColor(
                    requireContext(),
                    com.reuben.core_designsystem.R.color.transluscent_green
                )
                enableDashedLine(20F, 1F, 0F)
            }



        with(binding.chartTrend) {
            animateX(500, Easing.EaseInSine)
            description = null

            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = xValueFormatter
            xAxis.setDrawGridLines(false)

            xAxis.granularity = 1F
            xAxis.labelRotationAngle = 315F
            axisRight.isEnabled = false
            extraRightOffset = 30F
            this.data = LineData(lineDataSet)

        }
    }

    private fun setupNavigation() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        private val mostTradedCurrencies = mutableListOf(
            "USD" to "United States Dollar",
            "EUR" to "Euro",
            "JPY" to "Japanese Yen",
            "GBP" to "British Pound Sterling",
            "CNY" to "Chinese Yuan",
            "AUD" to "Australian Dollar",
            "CAD" to "Canadian Dollar",
            "CHF" to "Swiss Franc",
            "HKD" to "Hong Kong Dollar",
            "SGD" to "Singapore Dollar",
        )
    }
}