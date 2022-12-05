package com.hortopan.budgetlimitapp

import android.annotation.SuppressLint
import java.text.*
import java.util.*

class LocaleFormat {

    private val locale: Locale = Locale.getDefault()
    private val myDateFormat = "dd-MM-yyyy"

    @SuppressLint("SimpleDateFormat")
    val simpleDateFormat = SimpleDateFormat(myDateFormat)
    private val calendar = Calendar.getInstance()

    //Get the currency format for user localization
    fun getFormatCurrency(amount: Double): String? {

        val currencyFormat = NumberFormat.getCurrencyInstance(locale)

        //Get the currency code. Example "UAH"
        var currencyCode = Currency.getInstance(locale).toString()

        if(currencyCode == "RUB") currencyCode = "UAH"

        val currency: Currency = Currency.getInstance(currencyCode)
        val symbol = currency.getSymbol(locale)

        val decimalFormatSymbols: DecimalFormatSymbols =
            (currencyFormat as DecimalFormat).decimalFormatSymbols
        decimalFormatSymbols.currencySymbol = symbol
        currencyFormat.decimalFormatSymbols = decimalFormatSymbols
        return currencyFormat.format(amount).toString()
    }

    //Get the date format for user localization
    private fun getFormatDate(date: Date): String {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.SHORT, locale)
            .format(date).toString()
    }

    fun getDateForGraphRecycler(dateInString: String): String{
        val stringInDate = convertStringToDate(dateInString)
        return getFormatDate(stringInDate)
    }

    //Get string formatted date
    fun convertDataToString(date: Date): String{
        return simpleDateFormat.format(date)
    }

    //Convert String format date to Date format date
    fun convertStringToDate(dateInString: String): Date {
        calendar.time = simpleDateFormat.parse(dateInString) as Date

        //Get Date format date (Fri Nov 04 00:00:00 GMT+02:00 2022)
        return Date(calendar.timeInMillis)
    }


    fun countDay(date: Date, sum: Int): Date {
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, sum)
        return calendar.time
    }

    ////Get the time format for user localization
    fun getFormatTime(): String {

        val countryCodes = listOf("Ru", "BY", "UA", "KZ")
        val curTime = if(locale.country in countryCodes){
            SimpleDateFormat("HH:mm", locale).format(Date()).toString()
        }else{
            SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT, locale)
                .format(Date()).toString();
        }
        return curTime
    }

    //Get the current date in the format I need
    fun getDate(): String {
        return SimpleDateFormat(myDateFormat, locale).format(Date())
    }

}