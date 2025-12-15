package com.example.emptyviewsactivity.User

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.emptyviewsactivity.User.Fragment.ViewPageAdapter
import com.example.emptyviewsactivity.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class IlkUygulamaEkrani : AppCompatActivity() {
    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_ilk_uygulama_ekrani)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tabLayout = findViewById(R.id.tabLayout)
        viewPager2 = findViewById(R.id.viewPager2)

        val adapter = ViewPageAdapter(this)
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            when (position) {
                0 -> {
                    //tab.text = "Home"
                    tab.setIcon(R.drawable.baseline_home_24)
                }

                1 -> {
                    //tab.text = "Sepet"
                    tab.setIcon(R.drawable.baseline_shopping_cart)
                }

                2 -> {
                    //tab.text = "Ekle"
                    tab.setIcon(R.drawable.outline_add)
                }

                3 -> {
                    //tab.text = "Profil"
                    tab.setIcon(R.drawable.baseline_account_circle)
                }
            }
        }.attach()
    }
}