package com.reuben.feature_currency.ui.all_currencies

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reuben.core_common.date.fragment.viewBinding
import com.reuben.core_model.currency.Currency
import com.reuben.feature_currency.databinding.CurrencyItemBinding

class CurrenciesAdapter(
    private val onCurrencySelected: ((Currency) -> Unit)
) : ListAdapter<Currency, CurrenciesAdapter.CurrenciesViewHolder>(
    currencyDiffer
) {
    inner class CurrenciesViewHolder(
        private val binding: CurrencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currency: Currency) {
            with(binding) {
                textCurrencyCode.text = currency.currencyCode
                textCurrencyName.text = currency.currencyName

                root.setOnClickListener {
                    onCurrencySelected(currency)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        return CurrenciesViewHolder(
            parent.viewBinding(CurrencyItemBinding::inflate)
        )
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private val currencyDiffer = object : DiffUtil.ItemCallback<Currency>() {
    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.currencyCode.contentEquals(newItem.currencyCode)
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }

}