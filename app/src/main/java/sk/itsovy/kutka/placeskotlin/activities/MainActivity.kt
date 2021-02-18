package sk.itsovy.kutka.placeskotlin.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import sk.itsovy.kutka.placeskotlin.R
import sk.itsovy.kutka.placeskotlin.adapters.PlacesAdapter
import sk.itsovy.kutka.placeskotlin.database.DatabaseHandler
import sk.itsovy.kutka.placeskotlin.models.PlaceModel
import sk.itsovy.kutka.placeskotlin.utils.SwipeToDeleteCallback
import sk.itsovy.kutka.placeskotlin.utils.SwipeToEditCallback

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fabAddPlace.setOnClickListener {
            val intent = Intent(this@MainActivity, AddPlaceActivity::class.java)
            startActivityForResult(intent, ADD_PLACE_ACTIVITY_REQUEST_CODE)
        }
        getPlacesListFromDB()
    }

    private fun setupPlacesRecyclerView(placeList: ArrayList<PlaceModel>) {
        rv_places_list.layoutManager = LinearLayoutManager(this)
        rv_places_list.setHasFixedSize(true)

        val placesAdapter = PlacesAdapter(this, placeList)
        rv_places_list.adapter = placesAdapter

        placesAdapter.setOnClickListener(object: PlacesAdapter.OnClickListener {
            override fun onClick(position: Int, model: PlaceModel) {
                val intent = Intent(this@MainActivity, PlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)
            }
        })

        val editSwipeHandler = object: SwipeToEditCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_places_list.adapter as PlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }

        val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(rv_places_list)




        val deleteSwipeHandler = object: SwipeToDeleteCallback(this) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = rv_places_list.adapter as PlacesAdapter
                adapter.removeAt(viewHolder.adapterPosition)

                getPlacesListFromDB()
            }
        }

        val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
        deleteItemTouchHelper.attachToRecyclerView(rv_places_list)
    }

    private fun getPlacesListFromDB() {
        val dbHandler = DatabaseHandler(this)
        val getPlaceList: ArrayList<PlaceModel> = dbHandler.getPlacesList()

        if (getPlaceList.size > 0) {
            rv_places_list.visibility = View.VISIBLE
            tv_no_record.visibility = View.GONE
            setupPlacesRecyclerView(getPlaceList)
        } else {
            rv_places_list.visibility = View.GONE
            tv_no_record.visibility = View.VISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_PLACE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                getPlacesListFromDB()
            } else {
                Log.e("Activity", "Cancelled or Back Pressed.")
            }
        }
    }

    companion object {
       var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
       var EXTRA_PLACE_DETAILS = "extra_place_details"
    }
}