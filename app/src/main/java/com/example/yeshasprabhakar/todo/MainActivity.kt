package com.example.yeshasprabhakar.todo

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.Notification
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AbsListView
import android.widget.CompoundButton
import android.widget.DatePicker
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale // Added for SimpleDateFormat consistency

class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "10001"
        private const val DEFAULT_NOTIFICATION_CHANNEL_ID = "default"
        private const val TAG = "MainActivity"
    }

    private lateinit var todoViewModel: TodoViewModel
    private var items: MutableList<TodoItem> = mutableListOf() // Use MutableList
    private lateinit var itemsAdapter: ItemAdapter // Corrected from itemsAdopter
    private lateinit var itemsListView: ListView
    private lateinit var fab: FloatingActionButton
    private lateinit var toggleTheme: ToggleButton
    private lateinit var sharedPreferences: SharedPref // Already Kotlin

    override fun onCreate(savedInstanceState: Bundle?) {
        sharedPreferences = SharedPref(this)

        if (sharedPreferences.loadNightModeState()) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        todoViewModel = ViewModelProvider(this)[TodoViewModel::class.java] // Kotlin property access

        supportActionBar?.apply {
            setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            setCustomView(R.layout.actionbar)
        }

        toggleTheme = findViewById(R.id.themeActionButton)
        toggleTheme.isChecked = sharedPreferences.loadNightModeState()
        toggleTheme.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.setNightModeState(isChecked)
            restartApp()
        }

        fab = findViewById(R.id.fab)
        itemsListView = findViewById(R.id.itemsList)

        val emptyTextView: TextView = findViewById(R.id.emptyTextView)
        emptyTextView.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(getString(R.string.listEmptyText), Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(getString(R.string.listEmptyText))
        }
        val emptyView: FrameLayout = findViewById(R.id.emptyView)
        itemsListView.emptyView = emptyView

        itemsAdapter = ItemAdapter(this, items) // Pass mutable list
        itemsListView.adapter = itemsAdapter

        observeTodoList()
        onFabClick()
        hideFab()
    }

    private fun scheduleNotification(notification: Notification, delay: Long) {
        val notificationIntent = Intent(this, MyNotificationPublisher::class.java).apply {
            putExtra(MyNotificationPublisher.NOTIFICATION_ID, 1)
            putExtra(MyNotificationPublisher.NOTIFICATION, notification)
        }
        
        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // FLAG_IMMUTABLE available from M
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getBroadcast(this, 0, notificationIntent, pendingIntentFlag)
        
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.set(AlarmManager.RTC_WAKEUP, delay, pendingIntent)
        Log.d(TAG, "scheduleNotification: Notification set successfully!")
    }

    private fun getNotification(content: String): Notification {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntentFlag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // FLAG_IMMUTABLE available from M
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Matched flags with scheduleNotification for consistency
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, pendingIntentFlag)


        val builder = NotificationCompat.Builder(this, DEFAULT_NOTIFICATION_CHANNEL_ID).apply {
            setContentTitle("ToDo Reminder")
            setContentText(content)
            setContentIntent(pendingIntent)
            setAutoCancel(true)
            setSmallIcon(R.drawable.ic_stat_name) // Ensure this drawable exists
            setDefaults(Notification.DEFAULT_LIGHTS or Notification.DEFAULT_SOUND)
            // Channel ID is crucial for Android Oreo (API 26) and above.
            // Notification channels should be created when the app starts.
            // This example assumes the channel `NOTIFICATION_CHANNEL_ID` is created elsewhere.
            setChannelId(NOTIFICATION_CHANNEL_ID) 
            priority = NotificationCompat.PRIORITY_HIGH
        }
        return builder.build()
    }

    private fun restartApp() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finish()
        Log.d(TAG, "restartApp: Changed theme successfully")
    }

    private fun insertTodoItem(title: String, date: String, time: String) {
        val newTodo = TodoItem(name = title, date = date, time = time) // Use named arguments
        todoViewModel.insert(newTodo)
        toastMsg("Added successfully!")
        Log.d(TAG, "insertTodoItem: Item passed to ViewModel for insertion.")
    }

    private fun observeTodoList() {
        todoViewModel.allTodos.observe(this) { todoItems -> // Using trailing lambda
            itemsAdapter.setItems(todoItems ?: emptyList()) 
        }
        Log.d(TAG, "observeTodoList: Observer set up for LiveData")
    }

    fun deleteTodoItem(name: String, date: String, time: String) { 
        todoViewModel.deleteByNameDateTime(name, date, time)
        toastMsg("Item deleted")
        Log.d(TAG, "deleteTodoItem: Delete request sent to ViewModel for $name")
    }

    private fun hideFab() {
        itemsListView.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView?, scrollState: Int) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    fab.show()
                } else {
                    fab.hide()
                }
            }

            override fun onScroll(view: AbsListView?, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
                // Not used
            }
        })
    }

    private fun onFabClick() {
        fab.setOnClickListener {
            showAddDialog()
            Log.d(TAG, "onFabClick: Opened edit dialog")
        }
    }

    private fun showAddDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.custom_dialog, null)
        dialogBuilder.setView(dialogView)

        val editTitle: EditText = dialogView.findViewById(R.id.edit_title)
        val dateText: TextView = dialogView.findViewById(R.id.date)
        val timeText: TextView = dialogView.findViewById(R.id.time)

        val currentDateMillis = System.currentTimeMillis()
        val dateSdf = SimpleDateFormat("d MMMM", Locale.getDefault())
        dateText.text = dateSdf.format(currentDateMillis)

        val timeSdf = SimpleDateFormat("hh : mm a", Locale.getDefault())
        timeText.text = timeSdf.format(currentDateMillis)

        val cal = Calendar.getInstance().apply {
            timeInMillis = currentDateMillis
        }

        dateText.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@MainActivity,
                { _, year, monthOfYear, dayOfMonth ->
                    val newMonth = getMonth(monthOfYear) // monthOfYear is 0-indexed
                    dateText.text = "$dayOfMonth $newMonth"
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    Log.d(TAG, "onDateSet: Date has been set successfully")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate = currentDateMillis 
            datePickerDialog.show()
        }

        timeText.setOnClickListener {
            TimePickerDialog(
                this@MainActivity,
                { _, hourOfDay, minute ->
                    val formattedMinute = String.format(Locale.getDefault(), "%02d", minute)
                    val displayHour = when {
                        hourOfDay == 0 -> 12
                        hourOfDay > 12 -> hourOfDay - 12
                        else -> hourOfDay
                    }
                    val amPm = if (cal.get(Calendar.AM_PM) == Calendar.AM) "AM" else "PM" // Use Calendar AM_PM
                    
                    timeText.text = "$displayHour : $formattedMinute $amPm"
                    
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay) 
                    cal.set(Calendar.MINUTE, minute)
                    cal.set(Calendar.SECOND, 0)
                    Log.d(TAG, "onTimeSet: Time has been set successfully")
                },
                cal.get(Calendar.HOUR_OF_DAY), 
                cal.get(Calendar.MINUTE),
                false // explicitly false for 12hr format with AM/PM picker
            ).show()
        }

        dialogBuilder.setTitle("Let's add new task!")
        dialogBuilder.setPositiveButton("Done") { _, _ ->
            val title = editTitle.text.toString()
            val dateStr = dateText.text.toString() 
            val timeStr = timeText.text.toString() 

            if (title.isNotEmpty()) {
                try {
                    insertTodoItem(title, dateStr, timeStr)
                    scheduleNotification(getNotification(title), cal.timeInMillis)
                } catch (e: Exception) {
                    e.printStackTrace()
                    toastMsg("Error scheduling task.")
                }
            } else {
                toastMsg("Oops, Cannot set an empty ToDo!!!")
            }
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        val alertDialog = dialogBuilder.create()
        if (R.anim.slide_up != 0) { // Check if animation resource exists
            val animSlideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
            animSlideUp.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {}
                override fun onAnimationRepeat(animation: Animation?) {}
            })
            dialogView.startAnimation(animSlideUp)
         }
        alertDialog.show()
    }

    private fun toastMsg(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // month is 0-indexed (0-11)
    private fun getMonth(month: Int): String {
        return if (month in 0..11) DateFormatSymbols(Locale.getDefault()).months[month] else ""
    }
}
