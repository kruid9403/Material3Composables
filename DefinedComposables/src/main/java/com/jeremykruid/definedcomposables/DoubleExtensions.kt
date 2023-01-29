package com.jeremykruid.definedcomposables

import java.text.DecimalFormat


fun Double.convertDollarsCents(): String {
    val format = DecimalFormat("#,###.00")
    format.isDecimalSeparatorAlwaysShown = false
    return format.format(this).toString()
}