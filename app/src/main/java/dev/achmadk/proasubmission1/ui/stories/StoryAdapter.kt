package dev.achmadk.proasubmission1.ui.stories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import dev.achmadk.proasubmission1.R
import dev.achmadk.proasubmission1.models.StoryResponseBody

class StoryAdapter : PagingDataAdapter<StoryResponseBody, StoryViewHolder>(
    StoryItemCallback()
) {
    var onItemClick: ((StoryResponseBody) -> Unit)? = null

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(item)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.layout_story_row, parent, false)
        return StoryViewHolder(inflater)
    }
}