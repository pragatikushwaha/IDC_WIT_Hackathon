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
import android.view.View
import android.view.Window
import android.widget.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.microsoft.MainActivity.Companion.GroupID
import com.example.microsoft.MainActivity.Companion.UserName
import com.example.microsoft.MainActivity.Companion.listUser
import com.example.microsoft.room.TaskAdapter
import com.example.microsoft.room.database
import com.example.microsoft.room.entites.EventData
import com.example.microsoft.room.entites.TaskData
import kotlinx.android.synthetic.main.activity_task.*
import kotlinx.android.synthetic.main.dialog_event.*
import kotlinx.android.synthetic.main.dialog_task.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TaskActivity : AppCompatActivity() {
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        add_task.setOnClickListener {
                    customDialog()
        }

        val s = SpannableString("Task")
        s.setSpan(TypefaceSpan("avenir_ltstd_book.otf"), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = s

        given_task.setBackgroundResource(R.color.offWhite);
        assign_task.setBackgroundResource(R.color.white);

//        var data = database(this).getDao().getTaskDataGiven(UserName).observe()
//        if(data.value!= null) {
//            task_recyclerView.also {
//                it.layoutManager = LinearLayoutManager(this)
//                it.setHasFixedSize(true)
//                it.adapter = TaskAdapter(data.value!!)
//            }
//        }
        database(this).getDao().getTaskDataGiven(UserName)
            .observe(this, Observer { listdata->
                given_task.setBackgroundResource(R.color.offWhite);
                assign_task.setBackgroundResource(R.color.white);
                task_recyclerView.also {
                    it.layoutManager = LinearLayoutManager(this)
                    it.setHasFixedSize(true)
                    it.adapter = TaskAdapter(listdata)
                }
            })
        given_task.setOnClickListener {
            given_task.setBackgroundResource(R.color.offWhite);
            assign_task.setBackgroundResource(R.color.white);
            database(this).getDao().getTaskDataGiven(UserName)
                .observe(this, Observer { listdata->
                    task_recyclerView.also {
                        it.layoutManager = LinearLayoutManager(this)
                        it.setHasFixedSize(true)
                        it.adapter = TaskAdapter(listdata)
                    }
                })
        }
        assign_task.setOnClickListener {
            given_task.setBackgroundResource(R.color.white);
            assign_task.setBackgroundResource(R.color.offWhite);
            database(this).getDao().getTaskDataAssigned(UserName)
                .observe(this, Observer { listdata->
                    task_recyclerView.also {
                        it.layoutManager = LinearLayoutManager(this)
                        it.setHasFixedSize(true)
                        it.adapter = TaskAdapter(listdata)
                    }
                })
        }
    }
    private fun customDialog() {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_task)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        val task_name: TextView = dialog.task_name
        val task_assignedTo:Spinner= dialog.assigned_to
        val task_date: TextView = dialog.task_date
        val task_time: TextView = dialog.task_time
        val task_info: TextView = dialog.task_info
        val task_submit: Button = dialog.task_submit


        task_date.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            task_date.text = sdf.format(cal.time)

        }

        task_date.setOnClickListener {
            DatePickerDialog(this@TaskActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            task_time.text = SimpleDateFormat("HH:mm").format(cal.time)
        }

        task_time.setOnClickListener {
            TimePickerDialog(this@TaskActivity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(
                Calendar.MINUTE), true).show()
        }

        if (task_assignedTo != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, listUser)
            task_assignedTo.adapter = adapter

            task_assignedTo.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                   // Toast.makeText(this@TaskActivity,  " " +
                              //  "" + listUser[position], Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }


        task_submit.setOnClickListener {
            var taskData = TaskData(MainActivity.UserName,task_assignedTo.selectedItem.toString(), GroupID,task_name.text.toString(),task_date.text.toString(),task_time.text.toString(),task_info.text.toString())
            CoroutineScope(Dispatchers.IO).launch {
                database(applicationContext).getDao().addTaskData(taskData)
            }
            Toast.makeText(applicationContext, "Added Successfully", Toast.LENGTH_LONG).show()
            dialog.dismiss()

        }
    }
}
