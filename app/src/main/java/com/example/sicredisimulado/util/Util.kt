package com.example.sicredisimulado.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

class Util {

    fun hideKeyboard(context: Context, view: View) {
        val inputMethodManager: InputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }
}