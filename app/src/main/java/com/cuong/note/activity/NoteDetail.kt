package com.cuong.note.activity

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.res.ResourcesCompat
import com.cuong.note.database.Database
import com.cuong.note.Note
import com.cuong.note.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.text.SimpleDateFormat
import java.util.*

class NoteDetail : AppCompatActivity() {
    private lateinit var editTittle : EditText
    private lateinit var txtDate : TextView
    private lateinit var editContent :  EditText
    private lateinit var btnSave : ImageView
    private lateinit var note : Note
    private lateinit var imageBack : ImageView
    private lateinit var imageDelete: ImageView

    private var mode:Int = MODE_CREATE
    private var selectedNoteColor: String = "#333333"
    private var selectedNoteBackground: Int = 0
    private var selectedNoteTextColor: Int = 0


    companion object {
        private const val MODE_VIEW = 0
        private const val MODE_CREATE = 1
        private const val MODE_EDIT = 2
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.note_detail)

        initWidgets()
        initListener()
        selectedNoteBackground=getColor(R.color.colorPrimary)
        selectedNoteTextColor=getColor(R.color.colorWhite)

        initSelectColorLayout(false)
        setNoteColor()
        setColorOnClick()
        mode = this.intent.getIntExtra("MODE", MODE_CREATE)
        getModeNote()



    }

    private fun initListener() {
        imageBack.setOnClickListener { onBackPressed() }
        btnSave.setOnClickListener { saveNote() }
        imageDelete.setOnClickListener { deleteNote() }
    }

    private fun initWidgets() {
        editTittle = findViewById(R.id.inputNoteTitle)
        editContent= findViewById(R.id.inputNoteContent)
        txtDate= findViewById(R.id.textDateTime)
        btnSave = findViewById(R.id.imageSave)
        imageBack = findViewById(R.id.imageBack)
        imageDelete = findViewById(R.id.imageDelete)
    }

    private fun deleteNote() {
        if(mode == MODE_VIEW || mode == MODE_EDIT){
            val db = Database(this)
            db.deleteNote(note)
            setResult(Activity.RESULT_OK,this.intent)
            finish()
        }
    }

    private fun getModeNote() {
        if (mode == MODE_CREATE){
            editContent.setText("")
            editTittle.setText("")
            txtDate.text = getDateTime()
        }
        else if(mode == MODE_VIEW){
            note = this.intent.getSerializableExtra("NOTE") as Note
            editContent.setText(note.content)
            editTittle.setText(note.title)
            txtDate.text = note.date
            selectedNoteColor = note.color
            when(Color.parseColor(selectedNoteColor)){
               getColor(R.color.colorNoteDefaultColor)->{
                   selectedNoteBackground=getColor(R.color.colorPrimary)
                   selectedNoteTextColor = getColor(R.color.colorWhite)
               }
                getColor(R.color.colorNoteColor2)->{
                    selectedNoteBackground=getColor(R.color.colorNoteBackground2)
                    selectedNoteTextColor=getColor(R.color.colorNoteText2)
                }
                getColor(R.color.colorNoteColor3)->{
                    selectedNoteBackground=getColor(R.color.colorNoteBackground3)
                    selectedNoteTextColor=getColor(R.color.colorNoteText3)
                }
                getColor(R.color.colorNoteColor4)->{
                    selectedNoteBackground=getColor(R.color.colorNoteBackground4)
                    selectedNoteTextColor=getColor(R.color.colorNoteText4)
                }
                getColor(R.color.colorNoteColor5)->{
                    selectedNoteBackground=getColor(R.color.colorNoteBackground5)
                    selectedNoteTextColor=getColor(R.color.colorNoteText5)
                }
            }
            txtDate.visibility = View.VISIBLE
            setNoteColor()
            editMode(editTittle)
            editMode(editContent)

        }
    }

    private fun initSelectColorLayout(colorItemClicked : Boolean = false) {
        val layoutMiscellaneous  = findViewById<LinearLayout>(R.id.layoutMiscellaneous)
        val bottomSheetBehavior : BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(layoutMiscellaneous)
        layoutMiscellaneous.setOnClickListener {

        }
        val viewColorNoteSelect = findViewById<View>(R.id.viewSelectionColor)
        viewColorNoteSelect.setOnClickListener {
            if(bottomSheetBehavior.state!=BottomSheetBehavior.STATE_EXPANDED)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            else bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        if (colorItemClicked) bottomSheetBehavior.state=BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setColorOnClick() {

        val viewColor1 = findViewById<View>(R.id.viewColor1)
        val viewColor2 = findViewById<View>(R.id.viewColor2)
        val viewColor3 = findViewById<View>(R.id.viewColor3)
        val viewColor4 = findViewById<View>(R.id.viewColor4)
        val viewColor5 = findViewById<View>(R.id.viewColor5)
        viewColor1.setOnClickListener {
            selectedNoteColor = "#" + Integer.toHexString(
                ResourcesCompat.getColor(resources, R.color.colorNoteDefaultColor,null)
            )
            selectedNoteBackground = getColor(R.color.colorPrimary)
            selectedNoteTextColor =getColor(R.color.colorWhite)

            setNoteColor()
            initSelectColorLayout(true)
        }
        viewColor2.setOnClickListener {
            selectedNoteColor = "#" + Integer.toHexString(ResourcesCompat.getColor(resources, R.color.colorNoteColor2,null))
            selectedNoteBackground = getColor(R.color.colorNoteBackground2)
            selectedNoteTextColor =getColor(R.color.colorNoteText2)
            setNoteColor()
            initSelectColorLayout(true)
        }
        viewColor3.setOnClickListener {
            selectedNoteColor = "#" + Integer.toHexString(ResourcesCompat.getColor(resources, R.color.colorNoteColor3,null))
            selectedNoteBackground = getColor(R.color.colorNoteBackground3)
            selectedNoteTextColor =getColor(R.color.colorNoteText3)
            setNoteColor()
            initSelectColorLayout(true)
        }
        viewColor4.setOnClickListener {
            selectedNoteColor = "#" + Integer.toHexString(ResourcesCompat.getColor(resources, R.color.colorNoteColor4,null))
            selectedNoteBackground = getColor(R.color.colorNoteBackground4)
            selectedNoteTextColor =getColor(R.color.colorNoteText4)

            setNoteColor()
            initSelectColorLayout(true)
        }
        viewColor5.setOnClickListener {
            selectedNoteColor = "#" + Integer.toHexString(ResourcesCompat.getColor(resources, R.color.colorNoteColor5,null))
            selectedNoteBackground = getColor(R.color.colorNoteBackground5)
            selectedNoteTextColor =getColor(R.color.colorNoteText5)
            setNoteColor()
            initSelectColorLayout(true)
        }
    }

    private fun editMode(editText: EditText){
        editText.isFocusable = false
        editText.isFocusableInTouchMode = false
        editText.setOnClickListener{
            mode = MODE_EDIT
            editText.isFocusable = true
            editText.isFocusableInTouchMode = true
        }
    }

    private fun setNoteColor(){
        val coordinatorNote= findViewById<CoordinatorLayout>(R.id.coordinatorNote)
        coordinatorNote.setBackgroundColor(selectedNoteBackground)

        val gradientDrawable1 = editTittle.background as GradientDrawable
        gradientDrawable1.setColor(Color.parseColor(selectedNoteColor))

        val gradientDrawable2 = editContent.background as GradientDrawable
        gradientDrawable2.setColor(Color.parseColor(selectedNoteColor))

        val viewSelectionColor = findViewById<View>(R.id.viewSelectionColor)
        val gradientDrawable3 = viewSelectionColor.background as GradientDrawable

        gradientDrawable3.setColor(Color.parseColor(selectedNoteColor))

        editTittle.setTextColor(selectedNoteTextColor)
        editContent.setTextColor(selectedNoteTextColor)

    }

    private fun getDateTime(): String {
        return SimpleDateFormat("dd/MM/yyy hh:mm:ss",Locale.getDefault()).format(Date())
    }

    private fun saveNote() {
        when(mode){
            MODE_CREATE -> {

                val db = Database(this)
                val note = Note(title=editTittle.text.toString(),content = editContent.text.toString(),date = getDateTime(),color = selectedNoteColor)
                db.addNote(note)
                setResult(Activity.RESULT_OK,this.intent)
                finish()
            }
            MODE_EDIT -> {
                val db = Database(this)
                note.title =editTittle.text.toString()
                note.content = editContent.text.toString()
                note.color = selectedNoteColor
                db.updateNote(note)
                setResult(Activity.RESULT_OK,this.intent)
                finish()
            }
        }
    }
}