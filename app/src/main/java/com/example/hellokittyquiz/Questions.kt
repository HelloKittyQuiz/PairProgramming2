package com.example.hellokittyquiz
import androidx.annotation.StringRes
class Questions (@StringRes val textResId: Int, val answer: Boolean, var cheated: Boolean = false) {
}