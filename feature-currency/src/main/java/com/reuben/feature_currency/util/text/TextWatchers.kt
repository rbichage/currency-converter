package com.reuben.feature_currency.util.text

import android.text.Editable
import android.text.TextWatcher

class SourceAmountTextWatcher(private val onTextChanged: ((String) -> Unit)) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        val text = editable?.toString().orEmpty()

        if (text.isNotEmpty()) {
            onTextChanged(text)
        }
    }

}

class DestinationAmountTextWatcher(private val onTextChanged: ((String) -> Unit)) : TextWatcher {
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }

    override fun afterTextChanged(editable: Editable?) {
        val text = editable?.toString().orEmpty()

        if (text.isNotEmpty()) {
            onTextChanged(text)
        }
    }

}