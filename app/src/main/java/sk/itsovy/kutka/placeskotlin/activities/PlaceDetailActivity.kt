package sk.itsovy.kutka.placeskotlin.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_place_detail.*
import sk.itsovy.kutka.placeskotlin.R
import sk.itsovy.kutka.placeskotlin.models.PlaceModel

class PlaceDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        var placeDetailModel: PlaceModel? = null

        if (intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)) {
            placeDetailModel = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)!!
        }
        if (placeDetailModel != null) {
            setSupportActionBar(toolbar_place_detail)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.title = placeDetailModel.title

            toolbar_place_detail.setNavigationOnClickListener {
                onBackPressed()
            }

            iv_place_image.setImageURI(Uri.parse(placeDetailModel.image))
            tv_description.text = placeDetailModel.description
            tv_location.text = placeDetailModel.location

            btn_view_on_map.setOnClickListener{

                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, placeDetailModel)
                startActivity(intent)

            }
        }
    }
}