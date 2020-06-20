package com.banksampah.customer.ui.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.banksampah.customer.R
import com.banksampah.customer.model.History
import com.banksampah.customer.utils.RupiahFormatter
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    private val histories = ArrayList<History>()

    fun setData(histories: List<History>) {
        if (this.histories.isNotEmpty()) {
            this.histories.clear()
        }
        this.histories.addAll(histories)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = histories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(histories[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(history: History) {
            with(itemView) {
                if (history.type == "withdraw") {
                    tv_title.text = context.getString(R.string.withdraw_message)
                } else {
                    tv_title.text = context.getString(R.string.deposit_message)
                }
                tv_amount.text = RupiahFormatter.format(history.amount.toDouble())
                tv_date.text = history.date
            }
        }
    }
}