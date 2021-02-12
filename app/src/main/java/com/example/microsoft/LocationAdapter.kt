package com.example.microsoft

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.microsoft.LocationActivity.Companion.User_lat
import com.example.microsoft.LocationActivity.Companion.User_long
import com.example.microsoft.room.entites.LocationData
import kotlinx.android.synthetic.main.location_item_row.view.*

class LocationAdapter(private val Data: List<LocationData>) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {

    override fun getItemCount() = Data.size

    override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {
        holder.view.username.text =Data[position].username
        holder.view.last_date.text =(Data[position].lastTime).substring(0,10)
        holder.view.last_time.text =(Data[position].lastTime).substring(10)
        holder.view.distance.text =GetDistance(Data[position].lat,Data[position].longi)+ "  meter"
        holder.view.city.text =Data[position].cityName
        holder.view.Dist.text =Data[position].subAdminArea
        holder.view.state.text =Data[position].adminArea

    }
    fun GetDistance(lat:Double,long:Double):String
    {
        val startPoint = Location("locationA")
        startPoint.setLatitude(lat)
        startPoint.setLongitude(long)

        val endPoint = Location("locationA")
        endPoint.setLatitude(User_lat)
        endPoint.setLongitude(User_long)

         if(User_lat==0.0 && User_long==0.0)
             return "--"
        val distance = startPoint.distanceTo(endPoint)
        return distance.toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.location_item_row, parent, false)
        return ViewHolder(itemView)
    }

    //1
    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v), View.OnClickListener {
        //2
        var view: View = v

        //3
        init {
            v.setOnClickListener(this)
        }

        //4
        override fun onClick(v: View) {
            //Log.d("RecyclerView", "CLICK!")
        }


    }

}
