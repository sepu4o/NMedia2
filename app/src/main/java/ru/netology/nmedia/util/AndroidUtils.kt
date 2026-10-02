package ru.netology.nmedia.util

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.content.Context
import android.view.ViewTreeObserver


object AndroidUtils {

    fun formatCount(count: Int): String {
        return when {
            count < 1000 -> count.toString()
            count < 10_000 -> {
                val thousands = count / 1000
                val hundreds = (count % 1000) / 100
                if (hundreds == 0) "${thousands}K"
                else "${thousands}.${hundreds}K"
            }

            count < 1_000_000 -> {
                val thousands = count / 1000
                if (thousands < 10) "${thousands}K"
                else "${thousands}K"
            }

            count < 1_000_000_000 -> {
                val millions = count / 1_000_000
                val hundredsThousands = (count % 1_000_000) / 100_000
                if (hundredsThousands == 0) "${millions}M"
                else "${millions}.${hundredsThousands}M"
            }

            else -> "${count / 1_000_000_000}B"
        }
    }


    fun hideKeyboard(view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        view.requestFocus()
        if (view.hasWindowFocus()) {
            showKeyboardNow(view)
        } else {
            view.viewTreeObserver.addOnWindowFocusChangeListener(
                object : ViewTreeObserver.OnWindowFocusChangeListener {
                    override fun onWindowFocusChanged(hasFocus: Boolean) {
                        if (hasFocus) {
                            showKeyboardNow(view)
                            view.viewTreeObserver.removeOnWindowFocusChangeListener(this)
                        }
                    }
                }
            )
        }
    }

    private fun showKeyboardNow(view: View) {
        if (!view.isFocused) return
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}
