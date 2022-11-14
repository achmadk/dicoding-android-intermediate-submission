package dev.achmadk.proasubmission1

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.Intent.ACTION_GET_CONTENT
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import dev.achmadk.proasubmission1.databinding.FragmentCreateStoryBinding
import dev.achmadk.proasubmission1.models.StoryRequestBodyCreate
import dev.achmadk.proasubmission1.ui.create_story.viewmodels.CreateStoryViewModel
import dev.achmadk.proasubmission1.utils.Resource
import dev.achmadk.proasubmission1.utils.createCustomTempFile
import dev.achmadk.proasubmission1.utils.rotateBitmap
import dev.achmadk.proasubmission1.utils.uriToFile
import java.io.File

@AndroidEntryPoint
class CreateStoryFragment : Fragment() {

    private val viewModel: CreateStoryViewModel by viewModels()

    private var _binding: FragmentCreateStoryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        setupCreateStoryFormHandlers()
        observeViewModels()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupMenu() {
        (requireActivity() as MenuHost).addMenuProvider(object: MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_create_story, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.button_add -> {
                        val description = binding.edAddDescription.text.toString()
                        val requestBody = StoryRequestBodyCreate(description)
                        viewModel.submitStory(requestBody, photoFile)
                        return true
                    }
                }
                return true
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupCreateStoryFormHandlers() {
        binding.btnAddFileFromCamera.setOnClickListener {
            startTakePhoto()
        }
        binding.btnAddFileFromGallery.setOnClickListener {
            startGallery()
        }
    }

    private fun observeViewModels() {
        viewModel.submitStoryResponse.observe(viewLifecycleOwner) {
            val appContext = context?.applicationContext ?: return@observe
            when (it) {
                is Resource.Success -> {
                    toggleEnableCreateStoryForm(true)
                    findNavController().navigate(R.id.action_CreateStoryFragment_to_StoriesFragment)
                    Toast.makeText(context?.applicationContext, R.string.status_story_submitted_success, Toast.LENGTH_LONG).show()
                }
                is Resource.Error -> {
                    toggleEnableCreateStoryForm(true)
                    it.message?.let { message ->
                        Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
                    }
                }
                is Resource.Loading -> {
                    toggleEnableCreateStoryForm(false)
                }
            }
        }
    }

    private lateinit var currentPhotoPath: String

    private lateinit var photoFile: File

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            photoFile = File(currentPhotoPath)
            val imageBitmap = BitmapFactory.decodeFile(photoFile.path)
            binding.ivPhotoPreview.load(rotateBitmap(imageBitmap, true))
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            context?.let {
                val selectedImg: Uri = result.data?.data as Uri
                photoFile = uriToFile(selectedImg, it)
                binding.ivPhotoPreview.load(photoFile)
            }
        }
    }

    private fun startTakePhoto() {
        context?.let { it ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE)
            intent.resolveActivity(it.packageManager)

            createCustomTempFile(it).also { file ->
                val photoURI = FileProvider.getUriForFile(
                    it,
                    "dev.achmadk.proasubmission1",
                    file
                )
                currentPhotoPath = file.absolutePath
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                launcherIntentCamera.launch(intent)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.action_choose_photo_from_gallery))
        launcherIntentGallery.launch(chooser)
    }

    private fun toggleEnableCreateStoryForm(value: Boolean) {
        binding.btnAddFileFromCamera.isEnabled = value
        binding.btnAddFileFromGallery.isEnabled = value
    }

}