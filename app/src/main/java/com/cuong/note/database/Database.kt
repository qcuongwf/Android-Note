package com.cuong.note.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.cuong.note.Note
import kotlin.collections.ArrayList

class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "Note_Local"
        private const val TABLE_NOTE = "Note"
        private const val COLUMN_NOTE_ID = "id"
        private const val COLUMN_NOTE_TITLE = "title"
        private const val COLUMN_NOTE_CONTENT = "content"
        private const val COLUMN_NOTE_DATE = "date"
        private const val COLUMN_NOTE_COLOR = "color"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NOTE($COLUMN_NOTE_ID INTEGER PRIMARY KEY,$COLUMN_NOTE_TITLE TEXT,$COLUMN_NOTE_CONTENT TEXT,$COLUMN_NOTE_DATE TEXT, $COLUMN_NOTE_COLOR TEXT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTE")
        onCreate(db)
    }



    fun addNote(note: Note) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOTE_TITLE, note.title)
        values.put(COLUMN_NOTE_CONTENT, note.content)
        values.put(COLUMN_NOTE_DATE, note.date)
        values.put(COLUMN_NOTE_COLOR,note.color)
        db.insert(TABLE_NOTE, null, values)
        db.close()
    }




    val allNotes: List<Note>
        get() {
            val noteList: MutableList<Note> = ArrayList()
            // Select All Query
            val selectQuery = "SELECT  * FROM $TABLE_NOTE"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                do {
                    val note = Note()
                    note.id = cursor.getString(0).toInt()
                    note.title = cursor.getString(1)
                    note.content = cursor.getString(2)
                    note.date = cursor.getString(3)
                    note.color = cursor.getString(4)
                    // Adding note to list
                    noteList.add(note)
                } while (cursor.moveToNext())
            }
            cursor.close()
            return noteList
        }

    fun updateNote(note: Note): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NOTE_TITLE, note.title)
        values.put(COLUMN_NOTE_CONTENT, note.content)
        values.put(COLUMN_NOTE_DATE, note.date)
        values.put(COLUMN_NOTE_COLOR,note.color)

        // updating row
        return db.update(
            TABLE_NOTE,
            values,
            "$COLUMN_NOTE_ID = ?",
            arrayOf(note.id.toString())
        )
    }

    fun deleteNote(note: Note?) {
        val db = this.writableDatabase
        if (note != null) {
            db.delete(TABLE_NOTE, "$COLUMN_NOTE_ID = ?", arrayOf(note.id.toString()))
        }
        db.close()
    }
}