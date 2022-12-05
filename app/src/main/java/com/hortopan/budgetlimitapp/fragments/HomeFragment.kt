package com.hortopan.budgetlimitapp.fragments

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.hortopan.budgetlimitapp.BudgetViewModel
import com.hortopan.budgetlimitapp.BudgetViewModelFactory
import com.hortopan.budgetlimitapp.R
import com.hortopan.budgetlimitapp.adapter.ItemSpentDayAdapter
import com.hortopan.budgetlimitapp.data.BudgetDataBase
import com.hortopan.budgetlimitapp.data.tables.ItemMain
import com.hortopan.budgetlimitapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: BudgetViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = BudgetDataBase.getInstance(application).budgetDao
        val viewModelFactory = BudgetViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(BudgetViewModel::class.java)

        val adapterSpentDay = ItemSpentDayAdapter()
        initAdapterSpentDay(adapterSpentDay)

        initDataBase()
        viewModel.localeFormat.getFormatTime()
        initAdMob()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickButton()
    }

    override fun onResume() {
        super.onResume()
        binding.adViewHome.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.adViewHome.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adViewHome.destroy()
    }

    private fun initAdapterSpentDay(adapter: ItemSpentDayAdapter){
        binding.rvHome.adapter = adapter
        viewModel.itemSpentDayDao.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it.asReversed()
            }
        })
    }

    private fun onClickButton(){

        binding.btnMinus.setOnClickListener{
            onClickMinus()
        }
        binding.btnPlus.setOnClickListener{
            onClickPlus()
        }
        binding.btnNewBudget.setOnClickListener{
            alertNewBudget()
        }
    }

    private fun isNewDate(){
        if(viewModel.isNewDay()){
            viewModel.clearAndSetDefaultItemSpentDay()
            viewModel.isInsertItemSpentPeriod()
            viewModel.updateItemMainIfNewDay()
        }
    }

    private fun initDataBase(){
        viewModel.itemMainDao.observe(viewLifecycleOwner, Observer {
            val itemMain = it

            if(itemMain == null){
                viewModel.insertItemMain()
                viewModel.insertDefaultItemSpentDay()
            }else{
                viewModel.itemMainFromHome = itemMain
                isNewDate()
                fillMainBlock(itemMain)
            }
        })
    }

    private fun fillMainBlock(itemMain: ItemMain){
        binding.apply {
            itemMain.apply {
                val totalSum = viewModel.localeFormat.getFormatCurrency(totalSum)
                val totalDays = ("$curDay / $totalDays")
                val curSum = viewModel.localeFormat.getFormatCurrency(curSum)
                val recomdPerDay = viewModel.localeFormat.getFormatCurrency(recomdSum)
                val spentPerDay = "-${viewModel.localeFormat.getFormatCurrency(spentPerDay)}"

                tvSumTotalSum.text = totalSum
                tvSumTotalDays.text = totalDays
                tvSumCurSum.text = curSum
                tvSumRecomdPerDay.text = recomdPerDay
                tvSumSpentPerDay.text = spentPerDay
            }
        }
    }

    private fun getAddedSumClearInput(addedSum: String){
        viewModel.addedSum = addedSum.toDouble()
        binding.etAddCost.text.clear()
    }

    private fun onClickMinus(){

        val addedSum = binding.etAddCost.text.toString()
        if(viewModel.isEntryAddedSumValid(addedSum)){
            if(addedSum.toDouble() > 0){
                getAddedSumClearInput(addedSum)
                viewModel.onClickMinus()
                hideKeyboard()
            }

        }
    }

    private fun onClickPlus(){
        val addedSum = binding.etAddCost.text.toString()
        if(viewModel.isEntryAddedSumValid(addedSum)){
            if(addedSum.toDouble() > 0){
                getAddedSumClearInput(addedSum)
                viewModel.onClickPlus()
                hideKeyboard()
            }
        }
    }

    private fun hideKeyboard(){
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE)  as InputMethodManager?
        imm!!.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    /**
     * Alert Dialog when clicking on New Budget
     *  - Get new budget sum
     *  - Get the number of days for which the budget is calculated
     *  - The data of all tables will be deleted!
     */
    private fun alertNewBudget() {
        val inflater = layoutInflater
        val alertLayout = inflater.inflate(R.layout.layout_alert, null)
        val builder = AlertDialog.Builder(this.context)
            .setView(alertLayout)
            .setPositiveButton("Ok", null)
            .setNegativeButton(getString(R.string.cancel), null)
            .show()

        val budgetSum = alertLayout.findViewById<EditText>(R.id.et_budgetSum).text
        val budgetDays = alertLayout.findViewById<EditText>(R.id.et_budgetDays).text
        var showErrorSum = alertLayout.findViewById<TextView>(R.id.tv_errorSum)
        var showErrorDays = alertLayout.findViewById<TextView>(R.id.tv_errorDays)

        val mPositiveButton = builder.getButton(AlertDialog.BUTTON_POSITIVE)
        mPositiveButton.setOnClickListener {
            isEntryAlertValid(budgetSum.toString(), budgetDays.toString(), builder, showErrorSum, showErrorDays)
        }
    }

    private fun isEntryAlertValid(budgetSum: String,
                                  budgetDays: String,
                                  builder: AlertDialog,
                                  showErrorSum: TextView,
                                  showErrorDays: TextView
    ){
        if(budgetSum.isNotBlank() && budgetDays.isNotBlank()){
            viewModel.insertNewBudgetAction(budgetSum, budgetDays)
            hideKeyboard()
            builder.dismiss()
        }else if(budgetSum.isBlank()){
            showErrorSum.visibility = View.VISIBLE
        }else if(budgetDays.isBlank()){
            showErrorDays.visibility = View.VISIBLE
        }
    }

    private fun initAdMob(){
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adViewHome.loadAd(adRequest)
    }

    companion object {

        @JvmStatic
        fun newInstance() = HomeFragment()

    }

}