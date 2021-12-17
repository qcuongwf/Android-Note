package com.cuong.note

interface IClickItemOnListView {
    fun onItemNoteClick(note : Note)
    fun onItemLongClick(note: Note, position: Int): Boolean
}