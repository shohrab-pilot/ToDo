package com.example.yeshasprabhakar.todo

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("filename", Context.MODE_PRIVATE)

    //Save theme preference
    fun setNightModeState(state: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean("NightMode", state)
        editor.apply()
    }

    //Get theme preference
    fun loadNightModeState(): Boolean {
        return sharedPreferences.getBoolean("NightMode", false)
    }
}
