package com.cuong.note.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.cuong.note.*
import com.cuong.note.adapter.CustomAdapter
import com.cuong.note.database.Database
import com.cuong.note.datalocal.DataLocalManager
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), Filterable {

    private val noteList:MutableList<Note> = ArrayList()
    private var listNoteAll = noteList
    private lateinit var notesRecycleView : RecyclerView
    private lateinit var imageAddNoteMain: ImageView
    private lateinit var editSearch: EditText
    private lateinit var imageOption: ImageView
    private  lateinit var userAdapter: CustomAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initWidgets()
        initListener()
        displayListView(notesRecycleView,this@MainActivity)
        sortListView()
        loadSettings()
    }

    override fun onPause() {
        loadSettings()
        super.onPause()
    }

    override fun onRestart() {
        loadSettings()
        super.onRestart()
    }

    private fun loadSettings() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val checkDarkMode = sharedPreferences.getBoolean("darkMode",true)
        val itemHeight = sharedPreferences.getString("fontSize","medium")
        val mainBackground = findViewById<ConstraintLayout>(R.id.mainBackground)
        if(checkDarkMode){
            mainBackground.setBackgroundColor(getColor(R.color.colorPrimary))
        } else mainBackground.setBackgroundColor(getColor(R.color.colorWhite))
        when(itemHeight){

            "small"->{
                //listViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11f)
                userAdapter.setTextSizes(13)
            }
            "medium"->{
                //listViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
                userAdapter.setTextSizes(15)
            }
            "large" ->{
                //listViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                userAdapter.setTextSizes(30)
            }
        }
    }

    private fun initListener() {
        imageAddNoteMain.setOnClickListener {
            // Caller
            val intent = Intent(this, NoteDetail::class.java)
            val bundle = Bundle()
            bundle.putInt("MODE", MODE_CREATE)
            intent.putExtras(bundle)
            getResult.launch(intent)
        }

        editSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                getFilter().filter(s)
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        imageOption.setOnClickListener {
            showPopup(it)
        }
    }

    private fun initWidgets() {
        notesRecycleView = findViewById(R.id.notesRecycleView)
        imageAddNoteMain = findViewById(R.id.imageAddNoteMain)
        editSearch = findViewById(R.id.inputSearch)
        imageOption = findViewById(R.id.imageOption)
    }

    private fun sortMode(v : View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_sort, popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.sortAlphabetically -> DataLocalManager.setSortMode(1)
                R.id.sortDate -> DataLocalManager.setSortMode(2)
                R.id.sortColor -> DataLocalManager.setSortMode(3)
            }
            sortListView()
            Log.i("Info",DataLocalManager.getSortMode().toString())
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun sortListView() {
        when (DataLocalManager.getSortMode()) {
            //1 -> noteList.sortWith(kotlin.Comparator { o1, o2 -> o1.title.compareTo(o2.title) })
            1 -> noteList.sortWith { o1, o2 -> o1.title.compareTo(o2.title) }
            2 -> noteList.sortByDescending { note -> SimpleDateFormat("dd/MM/yyy hh:mm:ss", Locale.getDefault()).parse(note.date)}
            else -> noteList.sortByDescending { note -> note.color }
        }
        userAdapter.notifyDataSetChanged()
    }

    private fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.main_menu, popup.menu)
        popup.gravity = Gravity.CENTER
        popup.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.setting -> {
                    val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.sort ->{
                    //val viewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
                    sortMode(v)
                }
                R.id.view ->{
                    val popupView = PopupMenu(this@MainActivity,v,Gravity.CENTER)
                    val popupInflater = popupView.menuInflater as MenuInflater
                    popupInflater.inflate(R.menu.menu_view,popupView.menu)
                    popupView.show()
                    popupView.setOnMenuItemClickListener {
                        when(it.itemId){
                            R.id.viewGrid -> {
                                val recyclerView = findViewById<RecyclerView>(R.id.notesRecycleView)
                                DataLocalManager.setListViewMode(0)
                                Log.i("setViewMote",DataLocalManager.getListViewMode().toString())
                                recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
                            }
                            R.id.viewList ->{
                                val recyclerView = findViewById<RecyclerView>(R.id.notesRecycleView)
                                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                                DataLocalManager.setListViewMode(1)
                                Log.i("setViewMote",DataLocalManager.getListViewMode().toString())
                            }
                        }
                        true
                    }
                }
            }
            true
        }
        popup.show()
    }


    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) { result ->
            if(result.resultCode == Activity.RESULT_OK){
                refreshRecyclerView()
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshRecyclerView() {
        noteList.clear()
        val db = Database(this)
        val list = db.allNotes
        noteList.addAll(list)
        checkEmptyList(notesRecycleView)
        listNoteAll = noteList
        userAdapter.notifyDataSetChanged()
    }


    private fun displayListView(recyclerView: RecyclerView, context: Context) {
        val database = Database(context)
        val list = database.allNotes
        noteList.addAll(list)
        checkEmptyList(recyclerView)
        listNoteAll = noteList
        when(DataLocalManager.getListViewMode()){
            0 -> {
                recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)

            }
            1 -> {
                recyclerView.layoutManager = LinearLayoutManager(context)
                //recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            }
        }

        userAdapter = CustomAdapter(noteList, object : IClickItemOnListView {
            override fun onItemNoteClick(note: Note) {
                goToDetail(note, this@MainActivity)
            }

            override fun onItemLongClick(note: Note, position: Int): Boolean {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Delete")
                    .setMessage("Are you sure to delete this note?")
                    .setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
                    .setPositiveButton("yes") { _, _ ->
                        database.deleteNote(note)
                        refreshRecyclerView()
                    }
                    .create()
                    .show()
                return true
            }

        })
        recyclerView.adapter = userAdapter
    }

    private fun checkEmptyList(recyclerView: RecyclerView) {
        val listView = findViewById<TextView>(R.id.empty)
        if(noteList.isEmpty()){
            recyclerView.visibility = View.GONE
            listView.visibility = View.VISIBLE
        }
        else{
            recyclerView.visibility = View.VISIBLE
            listView.visibility = View.GONE
        }
    }


    private fun goToDetail(note: Note, context: Context) {
        val bundle = Bundle()
        bundle.putSerializable("NOTE",note)
        bundle.putInt("MODE", MODE_VIEW)
        startActivity(intent)
        val intent = Intent(context, NoteDetail::class.java)
        intent.putExtras(bundle)
        getResult.launch(intent)

    }
    companion object {
        private  const val MODE_VIEW = 0
        private const val MODE_CREATE = 1
    }


    override fun getFilter(): Filter {
        return filter
    }
    private  val filter = object : Filter(){
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filterListNote : MutableList<Note> = ArrayList()
            if (constraint.toString().isEmpty()){
                //filterListNote.addAll(listNoteAll)
                val database = Database(this@MainActivity)
                filterListNote.addAll(database.allNotes)
            }
            else {
                for(note in listNoteAll){
                    if (note.title.lowercase().contains(constraint.toString().lowercase())){
                        filterListNote.add(note)
                    }
                }
            }
            val results = FilterResults()
            results.values = filterListNote

            return results
        }

        @SuppressLint("NotifyDataSetChanged")
        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            noteList.clear()
            noteList.addAll(results?.values as Collection<Note>)
            userAdapter.notifyDataSetChanged()
        }

    }
}