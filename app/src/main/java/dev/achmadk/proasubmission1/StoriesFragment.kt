package dev.achmadk.proasubmission1

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.os.bundleOf
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.achmadk.proasubmission1.databinding.FragmentStoriesBinding
import dev.achmadk.proasubmission1.ui.stories.StoryAdapter
import dev.achmadk.proasubmission1.ui.stories.viewmodels.StoriesViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class StoriesFragment : Fragment() {
    private val viewModel: StoriesViewModel by viewModels()
    private var _binding: FragmentStoriesBinding? = null

    private lateinit var storyAdapter: StoryAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupUI() {
        setupMenu()
        setupRecyclerView()
        setupFloatingActionButton()
        observeViewModels()
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_stories, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_navigate_to_maps -> {
                        findNavController().navigate(R.id.action_StoriesFragment_to_StoryLocationsFragment)
                        return true
                    }
                    R.id.action_logout -> {
                        viewModel.logout()
                        findNavController().navigate(R.id.action_StoriesFragment_to_LoginFragment)
                        Toast.makeText(context?.applicationContext, R.string.status_logout_success, Toast.LENGTH_LONG).show()
                        return true
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupRecyclerView() {
        binding.rvStories.apply {
            layoutManager = LinearLayoutManager(context)
            val decoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            addItemDecoration(decoration)
            storyAdapter = StoryAdapter()
            adapter = storyAdapter
            storyAdapter.onItemClick = {
                val bundle = bundleOf("selectedStory" to it, "name" to it.name)
                findNavController().navigate(R.id.action_StoriesFragment_to_StoryDetailsFragment, bundle)
            }
        }
    }

    private fun setupFloatingActionButton() {
        binding.fabCreateStory.setOnClickListener {
            findNavController().navigate(R.id.action_StoriesFragment_to_CreateStoryFragment)
        }
    }

    private fun observeViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getStoriesPaging().observe(viewLifecycleOwner) {
                storyAdapter.submitData(lifecycle, it)
            }
        }
    }
}