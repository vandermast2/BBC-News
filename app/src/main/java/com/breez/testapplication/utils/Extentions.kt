package com.breez.testapplication.utils

import android.view.View


fun View.onClick(body: () -> Unit) {
    setOnClickListener { body() }
}