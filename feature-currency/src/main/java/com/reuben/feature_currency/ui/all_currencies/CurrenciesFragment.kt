package com.reuben.feature_currency.ui.all_currencies

import android.app.Dialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.reuben.core_common.date.fragment.viewBinding
import com.reuben.feature_currency.R
import com.reuben.feature_currency.databinding.FragmentCurrenciesBinding
import com.reuben.feature_currency.util.SELECTED_CURRENCY_REQUEST_KEY
import com.reuben.feature_currency.util.SELECTED_CURRENCY_TYPE_BUNDLE_KEY
import com.reuben.feature_currency.util.SELECTED_CURRENCY_VALUE_BUNDLE_KEY
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CurrenciesFragment : BottomSheetDialogFragment(R.layout.fragment_currencies) {
    private val binding by viewBinding(FragmentCurrenciesBinding::bind)

    private val args by navArgs<CurrenciesFragmentArgs>()
    private val currenciesAdapter by lazy {
        CurrenciesAdapter {
            Timber.e("currency $it")

            setFragmentResult(
                SELECTED_CURRENCY_REQUEST_KEY,
                bundleOf(
                    SELECTED_CURRENCY_TYPE_BUNDLE_KEY to args.currencyType,
                    SELECTED_CURRENCY_VALUE_BUNDLE_KEY to it
                )
            )
            dismiss()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener {
            val bottomSheet =
                dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)

            val params = bottomSheet.layoutParams
            val height: Int = Resources.getSystem().displayMetrics.heightPixels
            val maxHeight = (height * DIALOG_HEIGHT_PERCENT).toInt()
            params.height = maxHeight
            bottomSheet.layoutParams = params


            bottomSheet.setBackgroundColor(Color.TRANSPARENT)

            val behaviour = BottomSheetBehavior.from(bottomSheet)

            behaviour.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
                isDraggable = false
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.listCurrencies.adapter = currenciesAdapter
        currenciesAdapter.submitList(args.currencies.currencies)
        setupSearch()
    }

    private fun setupSearch() {
        binding.etSearchCurrency.doAfterTextChanged { editable ->
            val text = editable.toString()

            args.currencies.currencies.filter {
                it.currencyCode.contains(text, true) || it.currencyName.contains(text, true)
            }.apply {
                currenciesAdapter.submitList(this)
            }
        }
    }

    companion object {
        private const val DIALOG_HEIGHT_PERCENT = .80
    }

}