package com.putragandad.learnmaps

import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.function.DoubleBinaryOperator

class MapsFragment : Fragment() {

    private var locationArrayList : ArrayList<MapsData>? = null

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

        for (i in locationArrayList!!.indices) {
            googleMap.addMarker(MarkerOptions().position(LatLng(locationArrayList!![i].latitude, locationArrayList!![i].longitude)).title(locationArrayList!![i].placesName).snippet(locationArrayList!![i].placesAddress))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(locationArrayList!![2].latitude, locationArrayList!![2].longitude), 15.0f))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f), 2000, null)
            googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL
            googleMap.setOnMarkerClickListener {
                Toast.makeText(context, "Clicked! " + it.snippet + " " + it.title , Toast.LENGTH_SHORT).show()
                it.showInfoWindow()

                showBottomSheet(it.title, it.snippet, it.position.latitude, it.position.longitude)
                true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        locationArrayList = ArrayList()
        locationArrayList!!.add(MapsData(-2.961700781169533, 104.74892014785055, "Location 1", "Jalan Taqwa 1"))
        locationArrayList!!.add(MapsData(-2.9831295839366834, 104.74686021144464, "Location 2", "Jalan Taqwa 2"))
        locationArrayList!!.add(MapsData(-2.9814152950450232, 104.77484101429141, "Location 3", "Jalan Taqwa 3"))
    }

    private fun showBottomSheet(placeName: String?, placeAddress: String?, latitude: Double?, longitude: Double?) {
        var view = layoutInflater.inflate(R.layout.bottomsheet_maps, null)
        var dialog = BottomSheetDialog(requireContext())

        val tvPlaceName : TextView = view.findViewById(R.id.tv_placeName)
        val tvPlaceAddress : TextView = view.findViewById(R.id.tv_placeAddress)
        val tvLatitude: TextView = view.findViewById(R.id.tv_latitude)
        val tvLongitude: TextView = view.findViewById(R.id.tv_longitude)
        val btnGoogleMaps: Button = view.findViewById(R.id.btn_gmaps)

        tvPlaceName.text = placeName
        tvPlaceAddress.text = placeAddress
        tvLatitude.setText(latitude.toString())
        tvLongitude.setText(longitude.toString())

        btnGoogleMaps.setOnClickListener() {
            openGoogleMaps(latitude, longitude)
        }

        dialog.setContentView(view)
        dialog.show()

    }

    private fun openGoogleMaps(latitude: Double?, longitude: Double?) {
        var mapUri: Uri = Uri.parse("https://maps.google.com/maps/search/" + latitude + "," + longitude)
        val intent = Intent(Intent.ACTION_VIEW, mapUri)
        startActivity(intent)
    }

}

