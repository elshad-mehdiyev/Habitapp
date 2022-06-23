package com.baytar.elektron.habitapp.premium

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.baytar.elektron.habitapp.R

/**
 *
 */
class ViewPagerAdapter(
    /**
     *
     */
    val text: List<String>
) : RecyclerView.Adapter<ViewPagerAdapter.ViewPagerViewHolder>() {
    /**
     *
     */
    inner class ViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /**
         *
         */
        val textView: TextView = itemView.findViewById(R.id.quota1)

    }

    /**
     *
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_view_pager, parent, false)
        return ViewPagerViewHolder(view)
    }

    /**
     *
     */
    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        holder.textView.text = text[position]
    }

    /**
     *
     */
    override fun getItemCount(): Int {
        return text.size
    }
}