package dev.achmadk.proasubmission1

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import dev.achmadk.proasubmission1.databinding.FragmentStoryLocationsBinding
import dev.achmadk.proasubmission1.models.StoryResponseBody
import dev.achmadk.proasubmission1.ui.story_locations.viewmodels.StoryLocationViewModel
import dev.achmadk.proasubmission1.utils.Resource
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StoryLocationsFragment : Fragment() {
    private val storyLocationVieModel: StoryLocationViewModel by viewModels()
    private var _binding: FragmentStoryLocationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var gmap: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        gmap = googleMap
        getMyLastLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fusedLocationClient = activity?.let { LocationServices.getFusedLocationProviderClient(it) }!!
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    // No location access granted.
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return context?.let {
            ContextCompat.checkSelfPermission(
                it,
                permission
            )
        } == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (checkPermission(ACCESS_FINE_LOCATION) &&
            checkPermission(ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    showStartMarker(location)
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun showStartMarker(location: Location) {
        val startLocation = LatLng(location.latitude, location.longitude)
        gmap.addMarker(
            MarkerOptions()
                .position(startLocation)
                .title(getString(R.string.my_location_label))
        )
        gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 17f))
    }

    private fun setupUI() {
        observeViewModels()
    }

    private fun setStoriesMarker(stories: List<StoryResponseBody>) {
        for (story in stories) {
            val startLocation = LatLng(story.lat!!, story.lon!!)
            gmap.addMarker(
                MarkerOptions()
                    .position(startLocation)
                    .title("${story.name} - ${story.description}")
                    .snippet(story.photoUrl)
            )
        }
    }

    private fun observeViewModels() {
        viewLifecycleOwner.lifecycleScope.launch {
            storyLocationVieModel.getStoryLocations()

            storyLocationVieModel.storiesResponse.observe(viewLifecycleOwner) {
                val appContext = context?.applicationContext ?: return@observe
                when (it) {
                    is Resource.Success -> {
                        val stories = it.data!!
                        setStoriesMarker(stories)
                    }
                    is Resource.Error -> {
                        it.message?.let { message ->
                            Toast.makeText(appContext, message, Toast.LENGTH_LONG).show()
                        }
                    }
                    is Resource.Loading -> {
                        Toast.makeText(appContext, resources.getString(R.string.loading), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}