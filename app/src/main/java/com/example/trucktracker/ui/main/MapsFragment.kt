package com.example.trucktracker.ui.main

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.trucktracker.R
import com.example.trucktracker.data.Truck
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment() {

    companion object {
        fun newInstance() = MapsFragment()
        private const val NOT_RESPONDING_TIME = 14400 // 4 hours in sec
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private var truckList: ArrayList<Truck>? = null

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
        this.googleMap = googleMap
        if (this::googleMap.isInitialized) {
            this.googleMap = googleMap
            if (!truckList.isNullOrEmpty()) {
                updateLocations()
            }
        }
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun updateLocations() {

        if (!this::googleMap.isInitialized)
            return
        val truckIcon = BitmapFactory.decodeResource(requireContext().resources, R.drawable.van)
        truckList?.forEach { truck ->
            val drawable =
                DrawableCompat.wrap(resources.getDrawable(R.drawable.ic_truck_svg)).mutate()
            if (isNotResponding(truck)) {
                DrawableCompat.setTint(drawable, resources.getColor(R.color.red))
            } else if (truck.runningInfo.runningState == 0) {
                if (truck.positionInfo.isIgnition)
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.yellow))
                else
                    DrawableCompat.setTint(drawable, resources.getColor(R.color.blue))
            } else {
                DrawableCompat.setTint(drawable, resources.getColor(R.color.green))
            }

            val lat = truck.positionInfo.latitude
            val lng = truck.positionInfo.longitude
            val position = LatLng(lat.toDouble(), lng.toDouble())
            val markerOption = MarkerOptions().position(position).title(truck.truckNumber)


            markerOption.icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(drawable)))
            googleMap.addMarker(markerOption)

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(8.0f))
        }
    }

    private fun isNotResponding(truck: Truck): Boolean {
        val currentTime = System.currentTimeMillis()
        return (currentTime-truck.positionInfo.createTime)/1000 >= NOT_RESPONDING_TIME
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
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(callback)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        // TODO: Use the ViewModel
        viewModel.getTruckList().observe(viewLifecycleOwner) { trucks ->

            if (trucks == null) {
                return@observe
            }
            truckList = trucks as ArrayList
            updateLocations()
        }
    }
}