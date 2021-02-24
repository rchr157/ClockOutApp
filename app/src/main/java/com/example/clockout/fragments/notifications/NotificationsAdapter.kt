package com.example.clockout.fragments.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.clockout.database.TimeCardEntry
import com.example.clockout.databinding.NotificationListItemViewBinding

class NotificationsAdapter(val clickListener: NotificationsListener) : ListAdapter<TimeCardEntry, NotificationsAdapter.ViewHolder>(NotificationsDiffCallback()) {

    class NotificationsDiffCallback: DiffUtil.ItemCallback<TimeCardEntry>() {
        // Identify if an item has been updated/changed
        override fun areItemsTheSame(oldItem: TimeCardEntry, newItem: TimeCardEntry): Boolean {
            return oldItem.timeCardId == newItem.timeCardId
        }

        override fun areContentsTheSame(oldItem: TimeCardEntry, newItem: TimeCardEntry): Boolean {
            return oldItem ==newItem
        }
    }

    // Grab single item from list and attach it to the holder
    override  fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item, clickListener)
    }

    // Attaches the view holder created below to the adapter
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    // ViewHolder to manage data items before sending to recyclerview
    class ViewHolder private constructor(val binding: NotificationListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                // Attach item layout (list_item_sleep_night) to parent layout (recyclerview)
                val binding = NotificationListItemViewBinding.inflate(
                    layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
        // Format Data before sending to view
        fun bind(item: TimeCardEntry, clickListener: NotificationsListener) {
            binding.entry = item // need to tell the binding object about new entry
            binding.executePendingBindings() // execute any pending bindings right away
            binding.clickListener = clickListener
        }

    }

}

class NotificationsListener(val clickListener: (timeCardId: Long) -> Unit) {
    fun onClick(entry: TimeCardEntry) = clickListener(entry.timeCardId)
}