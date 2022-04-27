package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

class SelectLocationFragment : Fragment() {

    //Use Koin to get the view model of the SaveReminder
    private val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

//    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        map = googleMap

        map.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                requireContext(), R.raw.mapstyle
            )
        )

        enableMyLocation()


        //POI click
        setPoiClick(map)

        //The user make POIS
        setOurPois(map)

        // The user selected a Pois

        setOurPoiListener(map)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSelectLocationBinding.inflate(inflater)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)

//        TODO: add the map setup implementation
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // TODO: Change the map type based on the user's selection.
        R.id.normal_map -> {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
            true
        }
        R.id.hybrid_map -> {
            map.mapType = GoogleMap.MAP_TYPE_HYBRID
            true
        }
        R.id.satellite_map -> {
            map.mapType = GoogleMap.MAP_TYPE_SATELLITE
            true
        }
        R.id.terrain_map -> {
            map.mapType = GoogleMap.MAP_TYPE_TERRAIN
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    private fun enableMyLocation() {

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_LOCATION_PERMISSION
            )
            map.isMyLocationEnabled = false
//            if (runningQOrLater) {
//                        requestPermissions(
//                            arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
//                            REQUEST_LOCATION_PERMISSION
//                        )
//            }
        } else {
            map.isMyLocationEnabled = true
            getMyLocation()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED)
            ) {
                enableMyLocation()
                getMyLocation()
            } else {
                // Show a message with the alert for the default location
                Snackbar.make(
                    binding.root,
                    R.string.location_required_error, Snackbar.LENGTH_LONG
                ).setAction(android.R.string.ok) {
                    enableMyLocation()
                }.show()

                setDefaultLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val myLocation = LatLng(location.latitude, location.longitude)
                map.addMarker(MarkerOptions().position(myLocation).title("You are here!!"))
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))

            }
        }
    }

    private fun setDefaultLocation() {
        val myLocation = LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE)
        map.addMarker(MarkerOptions().position(myLocation).title("Default Location!!"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 12F))
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
        private const val DEFAULT_LATITUDE = 41.40338
        private const val DEFAULT_LONGITUDE = 2.17403
    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            )

            poiMarker?.showInfoWindow()
            _viewModel.selectedPOI.value = poi
            _viewModel.longitude.value = poi.latLng.longitude
            _viewModel.latitude.value = poi.latLng.latitude
            _viewModel.reminderSelectedLocationStr.value = poi.name

            findNavController().popBackStack()

        }

    }

    private fun setOurPois(map: GoogleMap) {
        map.setOnMapLongClickListener { latLng ->

            map.clear()

           val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Custom Poi!")
            )


        }
    }

    private fun setOurPoiListener(map: GoogleMap) {
        map.setOnMarkerClickListener { marker ->

            val poiMarker = map.addMarker(
                MarkerOptions()
                    .position(marker.position)
                    .title(marker.title)
            )

            poiMarker?.showInfoWindow()
            _viewModel.selectedPOI.value = PointOfInterest(marker.position,marker.id, marker.title ?: "")
            _viewModel.longitude.value = marker.position.longitude
            _viewModel.latitude.value = marker.position.latitude
            _viewModel.reminderSelectedLocationStr.value = marker.title

            findNavController().popBackStack()

            true
        }
    }


}
