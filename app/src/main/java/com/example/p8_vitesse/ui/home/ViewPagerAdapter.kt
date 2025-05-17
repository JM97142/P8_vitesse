package com.example.p8_vitesse.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.p8_vitesse.ui.home.candidatesFragments.AllCandidatesFragment
import com.example.p8_vitesse.ui.home.candidatesFragments.FavoritesFragment

class ViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    // Adaptateur pour gérer les fragments
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllCandidatesFragment() // Fragment affichant tous les candidats
            1 -> FavoritesFragment() // Fargment affichant les favoris
            else -> AllCandidatesFragment() // Par défaut affiche tous les candidats
        }
    }
    // Nombre total de fragments
    override fun getItemCount(): Int = 2
}