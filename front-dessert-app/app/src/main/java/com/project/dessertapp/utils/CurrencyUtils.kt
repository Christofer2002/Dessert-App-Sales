package com.project.dessertapp.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyUtils {
    val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale("es", "CR"))

    fun formatCurrency(value: Double): String {
        return currencyFormatter.format(value)
    }
}