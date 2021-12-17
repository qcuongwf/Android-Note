package com.cuong.note.datalocal

import android.content.Context




class SharedPreferences(var context: Context) {
    companion object{
        const val SHARED_PREFERENCES_NAME = "Setting"
    }
    fun putIntValue(key: String, mode :Int){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(key,mode)
        editor.apply()
    }
    fun getIntValue(key: String) : Int {
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        return sharedPreferences.getInt(key,0)
    }
    fun putBooleanValue(key: String, mode:Boolean){
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(key,mode)
        editor.apply()
    }
    fun getBooleanValue(key: String): Boolean{
        val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME,Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key,false)
    }

}