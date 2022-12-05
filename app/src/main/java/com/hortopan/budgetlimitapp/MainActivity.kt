package com.hortopan.budgetlimitapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.hortopan.budgetlimitapp.databinding.ActivityMainBinding
import com.hortopan.budgetlimitapp.fragments.FaqFragment
import com.hortopan.budgetlimitapp.fragments.GraphFragment
import com.hortopan.budgetlimitapp.fragments.HomeFragment


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Show HomeFragment on Start
        showFragment(HomeFragment.newInstance())

        buttonMenuClick()
    }

    private fun buttonMenuClick(){
        binding.bNav.setOnItemSelectedListener{
            when(it.itemId){
                R.id.item_home -> {
                    showFragment(HomeFragment.newInstance())
                }
                R.id.item_graph -> {
                    showFragment(GraphFragment.newInstance())
                }

                R.id.item_manual -> {
                    showFragment(FaqFragment.newInstance())
                }
            }
            true
        }
    }
    private fun showFragment(fragment: Fragment){

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.place_for_fragment, fragment)
            .commit()
    }
}