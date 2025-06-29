package com.example.tsukaeruzan

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var dataManager: DataManager
    private lateinit var assetAdapter: FinancialEntryAdapter
    private lateinit var expenseAdapter: FinancialEntryAdapter
    private lateinit var assets: MutableList<FinancialEntry>
    private lateinit var expenses: MutableList<FinancialEntry>

    private lateinit var tvTotalAssets: TextView
    private lateinit var tvTotalExpenses: TextView
    private lateinit var tvRemainingBalance: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataManager = DataManager(this)
        
        initViews()
        loadData()
        setupRecyclerViews()
        setupButtons()
        updateTotals()
    }

    private fun initViews() {
        tvTotalAssets = findViewById(R.id.tvTotalAssets)
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses)
        tvRemainingBalance = findViewById(R.id.tvRemainingBalance)
    }

    private fun loadData() {
        assets = dataManager.loadAssets()
        expenses = dataManager.loadExpenses()

        if (assets.isEmpty()) {
            assets.add(FinancialEntry(name = "預金", amount = 100000))
            assets.add(FinancialEntry(name = "現金", amount = 5000))
        }

        if (expenses.isEmpty()) {
            expenses.add(FinancialEntry(name = "家賃", amount = 50000))
            expenses.add(FinancialEntry(name = "食費", amount = 20000))
        }
    }

    private fun setupRecyclerViews() {
        val rvAssets: RecyclerView = findViewById(R.id.rvAssets)
        val rvExpenses: RecyclerView = findViewById(R.id.rvExpenses)

        assetAdapter = FinancialEntryAdapter(assets) {
            updateTotals()
            saveData()
        }

        expenseAdapter = FinancialEntryAdapter(expenses) {
            updateTotals()
            saveData()
        }

        rvAssets.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = assetAdapter
        }

        rvExpenses.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = expenseAdapter
        }
    }

    private fun setupButtons() {
        val btnAddAsset: Button = findViewById(R.id.btnAddAsset)
        val btnAddExpense: Button = findViewById(R.id.btnAddExpense)

        btnAddAsset.setOnClickListener {
            assetAdapter.addEntry()
            updateTotals()
        }

        btnAddExpense.setOnClickListener {
            expenseAdapter.addEntry()
            updateTotals()
        }
    }

    private fun updateTotals() {
        val totalAssets = assets.sumOf { it.amount }
        val totalExpenses = expenses.sumOf { it.amount }
        val remainingBalance = totalAssets - totalExpenses

        val formatter = NumberFormat.getNumberInstance(Locale.JAPAN)

        tvTotalAssets.text = getString(R.string.total_assets) + formatter.format(totalAssets) + getString(R.string.yen)
        tvTotalExpenses.text = getString(R.string.total_expenses) + formatter.format(totalExpenses) + getString(R.string.yen)
        tvRemainingBalance.text = getString(R.string.remaining_balance) + formatter.format(remainingBalance) + getString(R.string.yen)

        tvRemainingBalance.setTextColor(
            if (remainingBalance >= 0) 
                getColor(R.color.green) 
            else 
                getColor(R.color.red)
        )
    }

    private fun saveData() {
        dataManager.saveAssets(assets)
        dataManager.saveExpenses(expenses)
    }

    override fun onPause() {
        super.onPause()
        saveData()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveData()
    }
}