package com.hortopan.budgetlimitapp.data.tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_main")
data class ItemMain (
    @PrimaryKey val id: Int = 1,
    @ColumnInfo(name = "start_budget_date") var startBudgetDate: String = "empty",
    @ColumnInfo(name = "last_budget_date") var lastBudgetDate: String = "empty",
    @ColumnInfo(name = "last_date") var lastDate: String = "empty",
    @ColumnInfo(name = "total_sum") var totalSum: Double = 0.00,
    @ColumnInfo(name = "total_days") var totalDays: Int = 0,
    @ColumnInfo(name = "current_sum") var curSum: Double = 0.00,
    @ColumnInfo(name = "current_day") var curDay: Int = 0,
    @ColumnInfo(name = "recomd_sum") var recomdSum: Double = 0.00,
    @ColumnInfo(name = "spent_per_day") var spentPerDay: Double = 0.00

)