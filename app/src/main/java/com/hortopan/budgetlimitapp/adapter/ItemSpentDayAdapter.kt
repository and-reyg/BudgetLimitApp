package com.hortopan.budgetlimitapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.hortopan.budgetlimitapp.R
import com.hortopan.budgetlimitapp.data.tables.ItemSpentDay

class ItemSpentDayAdapter: RecyclerView.Adapter<ItemSpentDayAdapter.ItemSpentDayViewHolder>() {

    var data = listOf<ItemSpentDay>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : ItemSpentDayViewHolder = ItemSpentDayViewHolder.inflateFrom(parent)

    override fun onBindViewHolder(holder: ItemSpentDayViewHolder, position: Int) {
        val itemSpentDay = data[position]
        holder.bind(itemSpentDay)
    }

    class ItemSpentDayViewHolder(cardView: CardView): RecyclerView.ViewHolder(cardView){
        val time = cardView.findViewById<TextView>(R.id.tv_time)
        val sum = cardView.findViewById<TextView>(R.id.tv_sum)
        companion object{
            fun inflateFrom(parent: ViewGroup): ItemSpentDayViewHolder{
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.layout_home_cardview, parent, false) as CardView
                return ItemSpentDayViewHolder(view)
            }
        }
        fun bind(itemSpentDay: ItemSpentDay){
            time.text = itemSpentDay.time
            sum.text = itemSpentDay.sum
        }
    }
}