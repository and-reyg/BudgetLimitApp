package com.hortopan.budgetlimitapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hortopan.budgetlimitapp.data.tables.ItemMain
import com.hortopan.budgetlimitapp.data.tables.ItemSpentDay
import com.hortopan.budgetlimitapp.data.tables.ItemSpentPeriod


@Dao
interface BudgetDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMain(itemMain: ItemMain)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpentDay(itemSpentDay: ItemSpentDay)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSpentPeriod(itemSpentPeriod: ItemSpentPeriod)

    @Update
    suspend fun updateMain(itemMain: ItemMain)

    @Query("SELECT * from item_main WHERE id = 1")
    fun getItemMain(): LiveData<ItemMain>

    @Query("SELECT * from item_spent_period")
    fun getAllItemSpentPeriod(): LiveData<List<ItemSpentPeriod>>

    @Query("DELETE FROM item_spent_period")
    fun deleteAllFromItemSpentPeriod()


    @Query("SELECT * from item_spent_day")
    fun getAllItemSpentDay(): LiveData<List<ItemSpentDay>>

    @Query("DELETE FROM item_spent_day WHERE id = :id")
    fun deleteItemSpentDayById(id: Long)

    @Delete
    suspend fun deleteOneItemSpentDay(itemSpentDay: ItemSpentDay)

    @Query("DELETE FROM item_spent_day")
    fun deleteAllFromItemSpentDay()
}