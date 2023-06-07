package com.example.tutorial5.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Todo (
    var item:String?
){
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null
}