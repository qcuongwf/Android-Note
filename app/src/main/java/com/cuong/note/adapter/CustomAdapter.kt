package com.cuong.note.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cuong.note.IClickItemOnListView
import com.cuong.note.Note
import com.cuong.note.R


class CustomAdapter(private val listNote: MutableList<Note>, private val listener: IClickItemOnListView)  :
    RecyclerView.Adapter<CustomAdapter.CustomViewHolder>() {
    private var  textSize : Float = 12f
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.ls_it_tv_title)
        val tvContent: TextView = itemView.findViewById(R.id.ls_it_tv_content)
        val tvDate: TextView = itemView.findViewById(R.id.ls_it_tv_date)
        val lnListView: LinearLayout = itemView.findViewById(R.id.list_view_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.listview_item_main, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val note = listNote[position]
        holder.tvTitle.text = note.title
        holder.tvContent.text = note.content
        holder.tvDate.text = note.date
        holder.tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize)
        holder.tvContent.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize-1)
        holder.tvDate.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize-2)
        holder.lnListView.setOnClickListener {
            listener.onItemNoteClick(note)
        }
        holder.lnListView.setOnLongClickListener {
            listener.onItemLongClick(note,position)
        }
        val gradientDrawable: GradientDrawable = holder.lnListView.background as GradientDrawable
        if (note.color!=""){
            gradientDrawable.setColor(Color.parseColor(note.color))
        }else{
            gradientDrawable.setColor(Color.parseColor("#333333"))
        }
        if(note.color!="#333333"){
            holder.tvTitle.setTextColor(Color.parseColor("#000000"))
            holder.tvDate.setTextColor(Color.parseColor("#000000"))
            holder.tvContent.setTextColor(Color.parseColor("#000000"))
        }


    }
    @SuppressLint("NotifyDataSetChanged")
    fun setTextSizes(textSize: Int) {
        this.textSize = textSize.toFloat()
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int {
        return listNote.size
    }
}