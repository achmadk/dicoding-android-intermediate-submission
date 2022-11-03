package dev.achmadk.proasubmission1

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import dev.achmadk.proasubmission1.databinding.FragmentStoryDetailsBinding
import dev.achmadk.proasubmission1.models.StoryResponseBody

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
@AndroidEntryPoint
class StoryDetailsFragment : Fragment() {

    private var _binding: FragmentStoryDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStoryDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("DEPRECATION") val story: StoryResponseBody? = arguments?.getParcelable("selectedStory")

        setupUI(story)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI(story: StoryResponseBody?) {
        story?.let {
            binding.tvDetailName.text = story.name
            binding.tvDetailDescription.text = story.description
            binding.ivDetailPhoto.load(story.photoUrl)
        }
    }
}