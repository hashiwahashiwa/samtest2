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
        
        var nameWatcher: TextWatcher? = null
        var amountWatcher: TextWatcher? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_financial_entry, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]

        // Remove existing TextWatchers to prevent issues with view recycling
        holder.nameWatcher?.let { holder.etItemName.removeTextChangedListener(it) }
        holder.amountWatcher?.let { holder.etAmount.removeTextChangedListener(it) }

        // Set the text values
        holder.etItemName.setText(entry.name)
        holder.etAmount.setText(if (entry.amount == 0) "" else entry.amount.toString())

        // Create and set new TextWatchers
        holder.nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val currentPosition = holder.adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION && currentPosition < entries.size) {
                    entries[currentPosition].name = s.toString()
                    onDataChanged()
                }
            }
        }

        holder.amountWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val currentPosition = holder.adapterPosition
                if (currentPosition != RecyclerView.NO_POSITION && currentPosition < entries.size) {
                    entries[currentPosition].amount = s.toString().toIntOrNull() ?: 0
                    onDataChanged()
                }
            }
        }

        holder.etItemName.addTextChangedListener(holder.nameWatcher)
        holder.etAmount.addTextChangedListener(holder.amountWatcher)

        holder.btnDelete.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION && currentPosition < entries.size) {
                entries.removeAt(currentPosition)
                notifyItemRemoved(currentPosition)
                notifyItemRangeChanged(currentPosition, entries.size)
                onDataChanged()
            }
        }
    }

    override fun getItemCount() = entries.size

    fun addEntry() {
        entries.add(FinancialEntry())
        notifyItemInserted(entries.size - 1)
    }
}