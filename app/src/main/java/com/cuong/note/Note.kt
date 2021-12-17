package com.cuong.note

import java.io.Serializable

class Note(var id: Int =0,
           var title: String = "",
           var content: String = "",
           var date: String ="",
           var color: String ="") : Serializable