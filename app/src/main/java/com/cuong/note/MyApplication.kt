package com.cuong.note

import android.app.Application
import com.cuong.note.adapter.CustomAdapter
import com.cuong.note.datalocal.DataLocalManager

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        DataLocalManager.init(applicationContext)
    }
}