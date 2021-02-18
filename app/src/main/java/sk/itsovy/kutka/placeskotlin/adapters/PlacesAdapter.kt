package sk.itsovy.kutka.placeskotlin.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_place.view.*
import sk.itsovy.kutka.placeskotlin.R
import sk.itsovy.kutka.placeskotlin.activities.AddPlaceActivity
import sk.itsovy.kutka.placeskotlin.activities.MainActivity
import sk.itsovy.kutka.placeskotlin.database.DatabaseHandler
import sk.itsovy.kutka.placeskotlin.models.PlaceModel

open class PlacesAdapter(
        private val context: Context,
        private var list: ArrayList<PlaceModel>

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener: OnClickListener? = null


    /**
     * Inflatne item views, ktoré sa dizajnovali v xml layoute
     */

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(
                R.layout.item_place,
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    /**
     * Binding itemov v Arrayliste do view.
     * Zavolá sa, keď RecyclerView potrebuje nový {ViewHolder} daného typu.
     */

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.iv_place_image.setImageURI(Uri.parse(model.image))
            holder.itemView.tvTitle.text = model.title
            holder.itemView.tvDescription.text = model.description

            holder.itemView.setOnClickListener {
                if (onClickListener != null) {
                    onClickListener!!.onClick(position, model)
                }
            }
        }
    }

    fun notifyEditItem(activity: Activity, position: Int, requestCode: Int) {
        val intent = Intent(context, AddPlaceActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_PLACE_DETAILS, list[position])
        activity.startActivityForResult(intent, requestCode)
        notifyItemChanged(position)
    }

    /**
     * Name says it all.
     */
    override fun getItemCount(): Int {
        return list.size
    }

    fun removeAt(position: Int) {
        val dbHandler = DatabaseHandler(context)
        val isDeleted = dbHandler.deletePlace(list[position])
        if (isDeleted > 0) {
            list.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
    interface OnClickListener {
        fun onClick(position: Int, model: PlaceModel)
    }
}