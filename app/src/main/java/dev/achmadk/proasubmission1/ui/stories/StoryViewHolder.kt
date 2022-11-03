package dev.achmadk.proasubmission1.ui.stories

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.achmadk.proasubmission1.R
import dev.achmadk.proasubmission1.models.StoryResponseBody

class StoryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    private val tvItemName: TextView = view.findViewById(R.id.tv_item_name)
    private val ivItemPhoto: ImageView = view.findViewById(R.id.iv_item_photo)

    fun bind(data: StoryResponseBody) {
        tvItemName.text = data.name
        ivItemPhoto.load(data.photoUrl) {
            error(R.drawable.ic_baseline_image_not_supported)
        }
    }
}