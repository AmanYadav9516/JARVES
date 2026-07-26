package com.jarves.ai.core.controllers

import android.content.Context
import android.content.SharedPreferences

class MoneyLedgerController(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("jarves_money_ledger", Context.MODE_PRIVATE)

    fun recordMoney(personName: String, amount: Double, isReceive: Boolean = true): String {
        val key = personName.trim().lowercase()
        val currentAmount = prefs.getFloat(key, 0f)
        val newAmount = if (isReceive) currentAmount + amount.toFloat() else currentAmount - amount.toFloat()

        prefs.edit().putFloat(key, newAmount).apply()

        val actionText = if (isReceive) "receive from" else "pay to"
        return "Recorded: You need to $actionText ${personName.capitalize()} ₹${amount.toInt()}."
    }

    fun queryMoney(personName: String): String {
        val key = personName.trim().lowercase()
        val amount = prefs.getFloat(key, 0f)

        return if (amount > 0) {
            "Sir, you need to receive ₹${amount.toInt()} from ${personName.capitalize()}."
        } else if (amount < 0) {
            "Sir, you need to pay ₹${(-amount).toInt()} to ${personName.capitalize()}."
        } else {
            "Sir, there is no pending money record for ${personName.capitalize()}."
        }
    }

    private fun String.capitalize(): String {
        return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
