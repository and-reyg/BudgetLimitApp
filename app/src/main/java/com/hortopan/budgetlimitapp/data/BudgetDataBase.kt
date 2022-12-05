package com.hortopan.budgetlimitapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hortopan.budgetlimitapp.data.tables.ItemMain
import com.hortopan.budgetlimitapp.data.tables.ItemSpentDay
import com.hortopan.budgetlimitapp.data.tables.ItemSpentPeriod


@Database(entities = [ItemMain::class, ItemSpentDay::class, ItemSpentPeriod::class],
    version = 1,
    exportSchema = false)
abstract class BudgetDataBase: RoomDatabase() {

    abstract val budgetDao: BudgetDao
    companion object{
        @Volatile
        private var INSTANCE: BudgetDataBase? = null
        fun getInstance(context: Context):BudgetDataBase{
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BudgetDataBase::class.java,
                        "budget_database"

                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}