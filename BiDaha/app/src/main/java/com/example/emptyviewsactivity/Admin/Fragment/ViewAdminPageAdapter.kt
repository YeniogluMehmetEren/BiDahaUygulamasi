package com.example.emptyviewsactivity.Admin.Fragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.emptyviewsactivity.User.Fragment.AddProductFragment
import com.example.emptyviewsactivity.User.Fragment.HomeFragment
import com.example.emptyviewsactivity.User.Fragment.ProfilFragment
import com.example.emptyviewsactivity.User.Fragment.SepetFragment

class ViewAdminPageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> SepetFragment()
            2 -> AddProductFragment()
            3 -> AdminSettingsFragment()
            else -> ProfilFragment()
        }
    }
}