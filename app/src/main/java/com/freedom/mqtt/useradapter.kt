package com.freedom.mqtt

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.freedom.mqtt.model.user
import kotlinx.android.synthetic.main.chatlayout.view.*
import kotlinx.android.synthetic.main.grouplist.view.*

class useradapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<user>() {

        override fun areItemsTheSame(oldItem: user, newItem: user): Boolean {
           return oldItem.message==newItem.message
        }

        override fun areContentsTheSame(oldItem: user, newItem: user): Boolean {
            return oldItem==newItem
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return userviewholder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.chatlayout,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is userviewholder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<user>) {
        differ.submitList(list)
    }

    class userviewholder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: user) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            itemView.message.text=item.message


        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: user)
    }
}