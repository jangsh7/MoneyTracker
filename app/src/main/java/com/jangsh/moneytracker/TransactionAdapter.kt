package com.jangsh.moneytracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.time.format.DateTimeFormatter

class TransactionAdapter(
    private var items: List<Transaction>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private val dateFormatter = DateTimeFormatter.ofPattern("MM월 dd일")

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textType: TextView = view.findViewById(R.id.text_item_type)
        val textCategory: TextView = view.findViewById(R.id.text_item_category)
        val textAmount: TextView = view.findViewById(R.id.text_item_amount)
        val textDate: TextView = view.findViewById(R.id.text_item_date)
        val textMemo: TextView = view.findViewById(R.id.text_item_memo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.textType.text =
            if (item.type == TransactionType.INCOME) "수입" else "지출"
        holder.textCategory.text = item.category
        holder.textAmount.text = "${item.amount}원"
        holder.textDate.text = item.date.format(dateFormatter)
        holder.textMemo.text =
            if (item.memo.isBlank()) "메모 없음" else item.memo
    }

    fun submitList(newItems: List<Transaction>) {
        items = newItems
        notifyDataSetChanged()
    }
}
