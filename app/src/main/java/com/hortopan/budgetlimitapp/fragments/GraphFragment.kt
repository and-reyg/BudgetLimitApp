package com.hortopan.budgetlimitapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.hortopan.budgetlimitapp.BudgetViewModel
import com.hortopan.budgetlimitapp.BudgetViewModelFactory
import com.hortopan.budgetlimitapp.adapter.ItemSpentPeriodAdapter
import com.hortopan.budgetlimitapp.data.BudgetDataBase
import com.hortopan.budgetlimitapp.databinding.FragmentGraphBinding


class GraphFragment : Fragment() {
    private var _binding: FragmentGraphBinding? = null
    private val binding get() = _binding!!
    lateinit var viewModel: BudgetViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphBinding.inflate(inflater, container, false)
        val view = binding.root
        val application = requireNotNull(this.activity).application
        val dao = BudgetDataBase.getInstance(application).budgetDao
        val viewModelFactory = BudgetViewModelFactory(dao)
        viewModel = ViewModelProvider(
            this, viewModelFactory).get(BudgetViewModel::class.java)

        val adapterSpentPeriod = ItemSpentPeriodAdapter()
        initAdapterSpentPeriod(adapterSpentPeriod)
        initAdMob()
        return view
    }
    override fun onResume() {
        super.onResume()
        binding.adViewGraph.resume()
    }

    override fun onPause() {
        super.onPause()
        binding.adViewGraph.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.adViewGraph.destroy()
    }

    private fun initAdMob(){
        MobileAds.initialize(requireContext())
        val adRequest = AdRequest.Builder().build()
        binding.adViewGraph.loadAd(adRequest)
    }

    private fun initAdapterSpentPeriod(adapter: ItemSpentPeriodAdapter){
        binding.rvGraph.adapter = adapter
        viewModel.itemSpentPeriodDao.observe(viewLifecycleOwner, Observer {

            if(it.isEmpty()){
                binding.textLayoutIfGraphEmpty.visibility = View.VISIBLE
            }
            it?.let {
                adapter.data = it
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = GraphFragment()
    }
}