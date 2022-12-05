package com.hortopan.budgetlimitapp.data.tables


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_spent_period")
data class ItemSpentPeriod(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "date")
    var date: String = "",

    @ColumnInfo(name = "sum")
    var sum: String = ""
)