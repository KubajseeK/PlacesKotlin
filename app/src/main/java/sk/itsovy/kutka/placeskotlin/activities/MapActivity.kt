package sk.itsovy.kutka.placeskotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_map.*
import sk.itsovy.kutka.placeskotlin.R
import sk.itsovy.kutka.placeskotlin.models.PlaceModel

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mPlaceDetails: PlaceModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            mPlaceDetails = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS) as PlaceModel?
        }

        if (mPlaceDetails != null) {
            setSupportActionBar(toolbar_map)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = mPlaceDetails!!.title

            toolbar_map.setNavigationOnClickListener {
                onBackPressed()
            }

            val supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById((R.id.map)) as SupportMapFragment
            supportMapFragment.getMapAsync(this)

        }
    }

    override fun onMapReady(mMap: GoogleMap?) {
        val position = LatLng(mPlaceDetails!!.latitude, mPlaceDetails!!.longitude)
        mMap!!.addMarker(MarkerOptions().position(position).title(mPlaceDetails!!.title))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 15f)
        mMap.animateCamera(newLatLngZoom)
    }
}