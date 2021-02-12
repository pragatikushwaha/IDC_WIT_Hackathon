package com.example.microsoft

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.microsoft.room.database
import com.example.microsoft.room.entites.UserData
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mdrawlayout: DrawerLayout
    private lateinit var mtoggle: ActionBarDrawerToggle
    companion object {
        var UserName= ""
        var GroupID=0
        var listUser: ArrayList<String> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mdrawlayout = findViewById(R.id.Activity_drawer)
        mtoggle = ActionBarDrawerToggle(
            this, mdrawlayout,
            R.string.timesheet_open,
            R.string.timesheet_close
        )
        supportActionBar!!.elevation = (0).toFloat()
        val s = SpannableString("Family App")
        s.setSpan(TypefaceSpan("avenir_ltstd_book.otf"), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        supportActionBar!!.title = s
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        mdrawlayout.addDrawerListener(mtoggle)
        mtoggle.syncState()
        initializeNavigationView()
        val dateView: TextView = findViewById(R.id.dob)

        dateView.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd.MM.yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            dateView.text = sdf.format(cal.time)

        }

        dateView.setOnClickListener {
            DatePickerDialog(this@MainActivity, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        login.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                 UserName=username.text.toString()
                 GroupID=group_id.text.toString().toInt()
                 val user_data=UserData(username.text.toString(),group_id.text.toString().toInt(),dob.text.toString(),password.text.toString())
                database(applicationContext).getDao().addUserData(user_data)
                var userData= database(applicationContext).getDao().getUserDataByGroupId(GroupID)
                listUser.clear()
                CoroutineScope(Dispatchers.Main).launch {
                    for (i in 0..userData.size - 1) {
                        if(!listUser.contains(userData[i].username))
                        listUser.add(userData[i].username)
                    }
                    Log.d("ListUser Size", listUser.size.toString())
                    logged.visibility= View.VISIBLE
                    r1.visibility =View.INVISIBLE
                    r2.visibility=View.INVISIBLE
                    r3.visibility=View.INVISIBLE
                    r4.visibility =View.INVISIBLE
                    login.visibility=View.INVISIBLE
                    logged.text="Logged in Successfully as "+ UserName+" and GroupID is "+ GroupID;
                }
            }
        }

    }
    //Initialises the left Side Drawer .
    private fun initializeNavigationView() {
        Activity_nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.chat -> {
//                    val intent = Intent(applicationContext, WhiteBoardActivity::class.java)
//                    startActivity(intent)
                    Toast.makeText(applicationContext,"HI noti",Toast.LENGTH_SHORT).show()
                }
                R.id.task -> {
                    val intent = Intent(applicationContext, TaskActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                R.id.events -> {
//                    val intent = Intent(applicationContext, ApproverTimeSheetActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
//                    finish()
//                    Log.d("Version","In drawer approve")
                    val intent = Intent(applicationContext, EventActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
                R.id.reminder-> {
//                    val intent = Intent(applicationContext, ReportActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    startActivity(intent)
                }
                R.id.family_location -> {
                    val intent = Intent(applicationContext, LocationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
            mdrawlayout.closeDrawer(GravityCompat.START)
            true
        }
    }
}
