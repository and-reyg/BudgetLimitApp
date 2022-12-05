package com.hortopan.budgetlimitapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hortopan.budgetlimitapp.data.BudgetDao
import com.hortopan.budgetlimitapp.data.tables.ItemMain
import com.hortopan.budgetlimitapp.data.tables.ItemSpentDay
import com.hortopan.budgetlimitapp.data.tables.ItemSpentPeriod
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


class BudgetViewModel(val dao: BudgetDao): ViewModel() {

    var addedSum: Double = 0.0
    var itemMainFromHome: ItemMain = ItemMain()
    val itemMainDao = dao.getItemMain()
    val itemSpentDayDao = dao.getAllItemSpentDay()
    val itemSpentPeriodDao = dao.getAllItemSpentPeriod()
    val localeFormat = LocaleFormat()
    private val currentDate = localeFormat.getDate()

    fun insertItemMain(){
        val itemMain = ItemMain(
            id = 1,
            startBudgetDate = currentDate,
            lastBudgetDate = currentDate,
            lastDate = currentDate,
            totalSum = 0.0,
            totalDays = 0,
            curSum = 0.0,
            curDay = 0,
            recomdSum = 0.0,
            spentPerDay = 0.0
        )
        insertTableItemMain(itemMain)
    }

    fun isEntryAddedSumValid(addedSum: String): Boolean{
        return addedSum.isNotBlank()
    }

    fun insertNewBudgetAction(budgetSum: String, budgetDays: String){
        // write the new budget into ItemMain
        insertNewBudget(budgetSum.toDouble(), budgetDays.toInt())

        //Clearing the ItemSpentDay table and default entry
        clearAndSetDefaultItemSpentDay()

        //Clearing the ItemSpentPeriod
        clearItemSpentPeriod()
    }

    fun insertDefaultItemSpentDay(){
        val itemSpentDay = ItemSpentDay(
            id = 1,
            time = localeFormat.getFormatTime(),
            sum = localeFormat.getFormatCurrency(0.0).toString()
        )
        insertTableItemSpentDay(itemSpentDay)
    }


    fun clearAndSetDefaultItemSpentDay(){
        clearTableItemSpentDay()
        insertDefaultItemSpentDay()
    }

    private fun clearItemSpentPeriod(){
        clearTableSpentPeriod()
    }

    //Write a sequentially formatted amount to the ItemSpentDay table
    private fun addItemSpentDay(plusMinus: String){
        val itemSpentDay = ItemSpentDay(
            time = localeFormat.getFormatTime(),
            sum = "$plusMinus ${localeFormat.getFormatCurrency(addedSum)}"
        )
        insertTableItemSpentDay(itemSpentDay)
    }

    private fun insertItemSpentDayOnCLick(plusMinus: String){
        deleteItemSpentDayRow()
        addItemSpentDay(plusMinus)
    }

    private fun insertItemSpentPeriod(itemMain: ItemMain){
        val itemSpentPeriod = ItemSpentPeriod(
            date = localeFormat.getDateForGraphRecycler(itemMain.lastDate),
            sum = localeFormat.getFormatCurrency(itemMain.spentPerDay).toString()
        )
        insertTableItemSpentPeriod(itemSpentPeriod)
    }

    //checks if the sum for the previous day is bigger than zero then it will write into ItemSpentPeriod
    fun isInsertItemSpentPeriod(){
        val itemMain = itemMainFromHome
        if(itemMain.spentPerDay > 0) {
            insertItemSpentPeriod(itemMain)
        }

    }

    private fun updateOnclickMinus(){
        val addedSum = addedSum
        val itemMain = madeItemMain()

        itemMain.curSum = itemMain.curSum - addedSum
        itemMain.recomdSum = calculateRecomdSum(itemMain.curSum, itemMain.lastBudgetDate)
        itemMain.spentPerDay = itemMain.spentPerDay + addedSum
        updateTableItemMain(itemMain)
    }

    private fun updateOnClickPlus(){
        val addedSum = addedSum
        val itemMain = madeItemMain()

        itemMain.curSum = itemMain.curSum + addedSum
        itemMain.recomdSum = calculateRecomdSum(itemMain.curSum, itemMain.lastBudgetDate)
        updateTableItemMain(itemMain)
    }

    fun onClickMinus(){
        updateOnclickMinus()
        insertItemSpentDayOnCLick("-")
    }
    fun onClickPlus(){
        updateOnClickPlus()
        insertItemSpentDayOnCLick("+")
    }

    //Made ItemMain with general info for updateOnclickMinus and updateOnclickPlus
    private fun madeItemMain(): ItemMain {
        val itemMain = itemMainFromHome

        return ItemMain(
            id = 1,
            startBudgetDate = itemMain.startBudgetDate,
            lastBudgetDate = itemMain.lastBudgetDate,
            lastDate = currentDate,
            totalSum = itemMain.totalSum,
            totalDays = itemMain.totalDays,
            curSum = itemMain.curSum,
            curDay = getDayInOrder(itemMain.startBudgetDate),
            recomdSum = 0.0,
            spentPerDay = itemMain.spentPerDay
        )
    }

    fun updateItemMainIfNewDay(){
        val itemMain = itemMainFromHome
        itemMain.lastDate = currentDate
        itemMain.curDay = getDayInOrder(itemMain.startBudgetDate)
        itemMain.spentPerDay = 0.0
        updateTableItemMain(itemMain)
    }

    private fun insertNewBudget(totalSum: Double, totalDays: Int){
        val itemMain = ItemMain(
            id = 1,
            startBudgetDate = currentDate,
            lastBudgetDate = getLastBudgetDate(totalDays),
            lastDate = currentDate,
            totalSum = totalSum,
            totalDays = totalDays,
            curSum = totalSum,
            curDay = 1,
            recomdSum = roundUp2(totalSum / totalDays),
            spentPerDay = 0.0
        )
        updateTableItemMain(itemMain)
    }

    //round to 2 decimal places
    private fun roundUp2(number: Double): Double{
        return (number * 100.0).roundToInt().toDouble() / 100.0
    }


    fun isNewDay(): Boolean{
        //if the last recorded date is not equal to the current date, then this is a new day
        return itemMainFromHome.lastDate != currentDate
    }

    private fun getLastBudgetDate(totalDays: Int): String{

        val curDate = localeFormat.convertStringToDate(currentDate)

        //Add the number of days to the current date
        val lastBudgetDate = localeFormat.countDay(curDate, (totalDays-1))
        /**
         * totalDays-1 because the day the new budget is created is also taken into account
         * example: created 01.10 for 10 days, so the last day must be 10.10 but not 11.10
         */
        return localeFormat.convertDataToString(lastBudgetDate)
    }

    //Get the number of remaining budget days + the current day
    private fun getLeftDays(lastBudgetDate: String): Int {
        val lastDate = localeFormat.convertStringToDate(lastBudgetDate)
        val curDate = localeFormat.convertStringToDate(currentDate)
        return getDifferenceDate(lastDate, curDate) + 1
    }

    //Get the current day in order from the beginning ( example:  5 / 30)
    private fun getDayInOrder(startBudgetDate: String): Int {
        val startDate = localeFormat.convertStringToDate(startBudgetDate)
        val curDate = localeFormat.convertStringToDate(currentDate)
        return getDifferenceDate(curDate, startDate) + 1
    }

    private fun getDifferenceDate(date1: Date, date2: Date): Int{
        val milliseconds = date1.time - date2.time
        return (milliseconds / (24 * 60 * 60 * 1000)).toInt()
    }

    //calculate the recommended amount per day
    private fun calculateRecomdSum(curSum: Double, lastBudgetDate: String): Double{
        val recomdSum =  curSum / getLeftDays(lastBudgetDate)
        return roundUp2(recomdSum)
    }

    private fun updateTableItemMain(itemMain: ItemMain){
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateMain(itemMain)
        }
    }
    private fun insertTableItemMain(itemMain: ItemMain){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertMain(itemMain)
        }
    }
    private fun insertTableItemSpentDay(itemSpentDay: ItemSpentDay){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertSpentDay(itemSpentDay)
        }
    }

    private fun deleteItemSpentDayRow(){
        viewModelScope.launch(Dispatchers.IO){
            dao.deleteItemSpentDayById(1)
        }
    }
    private fun clearTableItemSpentDay(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllFromItemSpentDay()
        }
    }
    private fun clearTableSpentPeriod(){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteAllFromItemSpentPeriod()
        }
    }
    private fun insertTableItemSpentPeriod(itemSpentPeriod: ItemSpentPeriod){
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertSpentPeriod(itemSpentPeriod)
        }
    }

}