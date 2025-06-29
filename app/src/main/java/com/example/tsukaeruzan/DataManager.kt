package com.example.tsukaeruzan

import android.content.Context
import android.content.SharedPreferences

class DataManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("tsukaeruzan_data", Context.MODE_PRIVATE)

    fun saveAssets(assets: List<FinancialEntry>) {
        val jsonList = assets.map { it.toJson() }
        sharedPreferences.edit()
            .putStringSet("assets", jsonList.toSet())
            .apply()
    }

    fun loadAssets(): MutableList<FinancialEntry> {
        val jsonSet = sharedPreferences.getStringSet("assets", emptySet()) ?: emptySet()
        return jsonSet.mapNotNull { FinancialEntry.fromJson(it) }.toMutableList()
    }

    fun saveExpenses(expenses: List<FinancialEntry>) {
        val jsonList = expenses.map { it.toJson() }
        sharedPreferences.edit()
            .putStringSet("expenses", jsonList.toSet())
            .apply()
    }

    fun loadExpenses(): MutableList<FinancialEntry> {
        val jsonSet = sharedPreferences.getStringSet("expenses", emptySet()) ?: emptySet()
        return jsonSet.mapNotNull { FinancialEntry.fromJson(it) }.toMutableList()
    }
}