package com.example.microsoft

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.microsoft.room.entites.EventData
import kotlinx.android.synthetic.main.event_item_row.view.*

class EventAdapter(private val eventData: List<EventData>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    override fun getItemCount() = eventData.size

    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {
       holder.view.event_name.text =eventData[position].eventName
        holder.view.event_date.text =eventData[position].eventDate
        holder.view.event_time.text =eventData[position].eventTime
        holder.view.created_By.text =eventData[position].createdBy
        holder.view.event_info.text =eventData[position].eventInfo

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.event_item_row, parent, false)
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
