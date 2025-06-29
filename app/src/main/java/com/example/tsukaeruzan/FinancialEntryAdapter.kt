package com.example.tsukaeruzan

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView

class FinancialEntryAdapter(
    private val entries: MutableList<FinancialEntry>,
    private val onDataChanged: () -> Unit
) : RecyclerView.Adapter<FinancialEntryAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val etItemName: EditText = view.findViewById(R.id.etItemName)
        val etAmount: EditText = view.findViewById(R.id.etAmount)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_financial_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]

        holder.etItemName.setText(entry.name)
        holder.etAmount.setText(if (entry.amount == 0) "" else entry.amount.toString())

        holder.etItemName.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                entry.name = holder.etItemName.text.toString()
                onDataChanged()
            }
        }

        holder.etAmount.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val amountText = holder.etAmount.text.toString()
                entry.amount = amountText.toIntOrNull() ?: 0
                onDataChanged()
            }
        }

        holder.etItemName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                entry.name = s.toString()
                onDataChanged()
            }
        })

        holder.etAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                entry.amount = s.toString().toIntOrNull() ?: 0
                onDataChanged()
            }
        })

        holder.btnDelete.setOnClickListener {
            entries.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
            notifyItemRangeChanged(holder.adapterPosition, entries.size)
            onDataChanged()
        }
    }

    override fun getItemCount() = entries.size

    fun addEntry() {
        entries.add(FinancialEntry())
        notifyItemInserted(entries.size - 1)
    }
}