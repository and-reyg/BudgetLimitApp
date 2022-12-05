package com.hortopan.budgetlimitapp.data.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_spent_day")
data class ItemSpentDay(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "time")
    var time: String = "",

    @ColumnInfo(name = "sum")
    var sum: String = ""
)