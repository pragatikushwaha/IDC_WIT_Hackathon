package com.example.microsoft

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.microsoft.MainActivity.Companion.GroupID
import com.example.microsoft.MainActivity.Companion.UserName
import com.example.microsoft.room.database
import com.example.microsoft.room.entites.EventData
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.dialog_event.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class EventActivity : AppCompatActivity() {
    lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)
        add_event.setOnClickListener {
            customDialog()
        }

        val s = SpannableString("Events")
        s.setSpan(TypefaceSpan("avenir_ltstd_book.otf"), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = s

        database(this).getDao().getEventData()
            .observe(this, Observer { listdata->
                event_recyclerView.also {
                    it.layoutManager = LinearLayoutManager(this)
                    it.setHasFixedSize(true)
                    it.adapter = EventAdapter(listdata)
                }
            })
    }
    private fun customDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_event)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val event_name: TextView = dialog.event_name
        val event_date: TextView = dialog.event_date
        val event_time: TextView = dialog.event_time
        val event_info: TextView = dialog.event_info
        val event_submit: Button = dialog.event_submit


        event_date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            event_date.text = sdf.format(cal.time)

        }

        event_date.setOnClickListener {
            DatePickerDialog(this@EventActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            event_time.text = SimpleDateFormat("HH:mm").format(cal.time)
        }

        event_time.setOnClickListener {
            TimePickerDialog(this@EventActivity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
        }

        event_submit.setOnClickListener {
             var eventData = EventData(UserName, GroupID,event_name.text.toString(),event_date.text.toString(),event_time.text.toString(),event_info.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                database(applicationContext).getDao().addEventData(eventData)
            }
            Toast.makeText(applicationContext, "Added Successfully", Toast.LENGTH_LONG).show()
            dialog.dismiss()

        }
    }
}
