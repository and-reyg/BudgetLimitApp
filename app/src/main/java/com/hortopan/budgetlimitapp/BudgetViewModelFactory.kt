package com.hortopan.budgetlimitapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hortopan.budgetlimitapp.data.BudgetDao

class BudgetViewModelFactory(private val dao: BudgetDao): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(BudgetViewModel::class.java)){
            return BudgetViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel")
    }
}