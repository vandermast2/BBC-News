package com.breez.testapplication.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

internal const val NO_FLAGS = 0

fun Activity.hideKeyboard() {
    currentFocus?.let {
        val iim = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        iim.hideSoftInputFromWindow(it.windowToken, NO_FLAGS)
    }
}

fun Fragment.hideKeyboard() {
    activity?.hideKeyboard()
}
