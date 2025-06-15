package com.example.p8_vitesse.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.example.p8_vitesse.R
import com.example.p8_vitesse.databinding.ActivityMainBinding
import com.example.p8_vitesse.ui.addCandidate.AddCandidateActivity
import com.example.p8_vitesse.ui.home.ViewPagerAdapter
import com.example.p8_vitesse.ui.home.candidatesFragments.AllCandidatesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: AllCandidatesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Applique les marges système (barres de statut/navigation)
        binding = ActivityMainBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setContentView(binding.root)

        setupSearch() // Barre de recherche
        setTableLayout() // Tabs et ViewPager
        setupFab() // Bouton ajout candidat
    }

    // Configuration de la logique de recherche
    private fun setupSearch() {
        binding.searchEditText.addTextChangedListener { editable ->
            val query = editable?.toString().orEmpty()
            viewModel.setQuery(query)
        }
    }

    // Configuration des onglets (All / favorites)
    private fun setTableLayout() {
        val adapter = ViewPagerAdapter(this)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tablelayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_item_1)
                1 -> tab.text = getString(R.string.tab_item_2)
            }
        }.attach()
    }

    // Configuration du bouton ajout d'un candidat
    private fun setupFab() {
        val fab = findViewById<FloatingActionButton>(R.id.fab_add)
        fab.setOnClickListener {
            val intent = Intent(this, AddCandidateActivity::class.java)
            startActivity(intent)
        }
    }
}