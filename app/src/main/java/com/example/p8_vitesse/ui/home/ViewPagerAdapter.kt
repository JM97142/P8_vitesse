package com.example.p8_vitesse.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.p8_vitesse.ui.home.candidatesFragments.AllCandidatesFragment
import com.example.p8_vitesse.ui.home.candidatesFragments.FavoritesFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllCandidatesFragment()
            1 -> FavoritesFragment()
            else -> AllCandidatesFragment()
        }
    }

    override fun getItemCount(): Int = 2
}