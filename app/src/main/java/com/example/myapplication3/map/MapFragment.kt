package com.example.myapplication3.map

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.myapplication3.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_map.*


class MapFragment : Fragment(), OnMapReadyCallback{

    private lateinit var googleMap : GoogleMap
    private val REQUEST_LOCATION_PERMISSION = 1

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        map_view.onCreate(savedInstanceState)
        map_view.onResume()

        map_view.getMapAsync(this)
    }
    override fun onMapReady(map: GoogleMap?) {
        map?.let{
            googleMap = it
            /*googleMap.isMyLocationEnabled = true*/
            val location = LatLng(3.215133, 101.728426)
            googleMap.addMarker((MarkerOptions().position(location).title("EZ SHIPPING HQ")))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location,5f))

            val location1 = LatLng(5.415616, 100.322744)
            googleMap.addMarker((MarkerOptions().position(location1).title("PENANG BRANCH")))

            val location2 = LatLng(2.195947, 102.247380)
            googleMap.addMarker((MarkerOptions().position(location2).title("MALACCA BRANCH")))

            val location3 = LatLng(2.726522, 101.937697)
            googleMap.addMarker((MarkerOptions().position(location3).title("SEREMBAN BRANCH")))

        }
        enableMyLocation()
    }
    // Checks that users have given permission
    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // Checks if users have given their location and sets location enabled if so.
    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            googleMap.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    // Callback for the result from requesting permissions.
    // This method is invoked for every call on requestPermissions(android.app.Activity, String[],
    // int).
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                enableMyLocation()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }
}
