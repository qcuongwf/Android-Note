package com.cuong.note.datalocal

import android.content.Context

class DataLocalManager {
    private var sharedPreferences: SharedPreferences? = null

    companion object {
        private const val LIST_VIEW_MODE = "listViewMode"
        private const val SORT_MODE = "SortMode"
        private var instance: DataLocalManager? = null
        fun init(context: Context?) {
            instance = DataLocalManager()
            instance!!.sharedPreferences = SharedPreferences(
                context!!
            )
        }

        fun getInstance(): DataLocalManager? {
            if (instance == null) instance = DataLocalManager()
            return instance
        }

        var listViewMode: Int
            get() = getInstance()!!.sharedPreferences!!.getIntValue(LIST_VIEW_MODE)
            set(mode) {
                getInstance()!!.sharedPreferences!!.putIntValue(LIST_VIEW_MODE, mode)
            }
        var sortMode: Int
            get() = getInstance()!!.sharedPreferences!!.getIntValue(SORT_MODE)
            set(mode) {
                getInstance()!!.sharedPreferences!!.putIntValue(SORT_MODE, mode)
            }
    }
}