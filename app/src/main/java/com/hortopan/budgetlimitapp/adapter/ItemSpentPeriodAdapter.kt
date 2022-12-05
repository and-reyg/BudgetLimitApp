package com.hortopan.budgetlimitapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hortopan.budgetlimitapp.R
import com.hortopan.budgetlimitapp.data.tables.ItemSpentPeriod

class ItemSpentPeriodAdapter: RecyclerView.Adapter<ItemSpentPeriodAdapter.ItemSpentPeriodViewHolder>() {

    var data = listOf<ItemSpentPeriod>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemSpentPeriodViewHolder = ItemSpentPeriodViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ItemSpentPeriodViewHolder, position: Int) {

        val itemSpentPeriod = data[position]
        holder.bind(itemSpentPeriod)
    }

    class ItemSpentPeriodViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView) {
        val date = cardView.findViewById<TextView>(R.id.tv_dateGraph)
        val sum = cardView.findViewById<TextView>(R.id.tv_sumGraph)

        companion object{
            fun inflateFrom(parent: ViewGroup): ItemSpentPeriodViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.layout_graph_cardview, parent, false) as CardView
                return ItemSpentPeriodViewHolder(view)
            }
        }
        fun bind(itemSpentPeriod: ItemSpentPeriod){

            date.text = itemSpentPeriod.date
            sum.text = itemSpentPeriod.sum
        }
    }
}