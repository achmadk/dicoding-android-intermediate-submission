package dev.achmadk.proasubmission1.ui.stories

import androidx.recyclerview.widget.DiffUtil
import dev.achmadk.proasubmission1.models.StoryResponseBody

class StoryItemCallback: DiffUtil.ItemCallback<StoryResponseBody>() {
    override fun areItemsTheSame(
        oldItem: StoryResponseBody,
        newItem: StoryResponseBody
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: StoryResponseBody,
        newItem: StoryResponseBody
    ): Boolean {
        return oldItem == newItem
    }
}