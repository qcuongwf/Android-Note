package com.cuong.note.datalocal;

import android.content.Context;

public class DataLocalManager {
    private static final String  LIST_VIEW_MODE = "listViewMode";
    private static final String SORT_MODE = "SortMode";
    private static final String DARK_MODE = "darkMode";
    private static DataLocalManager instance;
    private SharedPreferences sharedPreferences;
    public static void init(Context context){
        instance = new DataLocalManager();
        instance.sharedPreferences = new SharedPreferences(context);
    }
    public static DataLocalManager getInstance(){
        if (instance == null)
            instance = new DataLocalManager();
        return instance;
    }
    public static void setListViewMode(int mode){
        DataLocalManager.getInstance().sharedPreferences.putIntValue(LIST_VIEW_MODE,mode);
    }
    public static int getListViewMode(){
        return DataLocalManager.getInstance().sharedPreferences.getIntValue(LIST_VIEW_MODE);
    }

    public static void setSortMode(int mode){
        DataLocalManager.getInstance().sharedPreferences.putIntValue(SORT_MODE,mode);
    }
    public static int getSortMode(){
        return DataLocalManager.getInstance().sharedPreferences.getIntValue(SORT_MODE);
    }

    public static void setDarkMode(Boolean mode){
        DataLocalManager.getInstance().sharedPreferences.putBooleanValue(DARK_MODE,mode);
    }
    public static Boolean getDardMode(){
        return DataLocalManager.getInstance().sharedPreferences.getBooleanValue(DARK_MODE);
    }
}
