package com.example.p8_vitesse.ui.home.candidatesFragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p8_vitesse.databinding.RecyclerCandidatesBinding
import com.example.p8_vitesse.ui.candidateDetails.CandidateDetailActivity
import com.example.p8_vitesse.ui.home.CandidatesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllCandidatesFragment: Fragment() {
    private var _binding: RecyclerCandidatesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllCandidatesViewModel by activityViewModels()
    private lateinit var candidatesAdapter: CandidatesAdapter

    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val deletedId = result.data?.getLongExtra("deleted_id", -1L) ?: -1L
            if (deletedId != -1L) {
                viewModel.deleteCandidate(deletedId)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RecyclerCandidatesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loading.visibility = View.VISIBLE // Affiche le loader

        setRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filtered.collect { candidates ->
                candidatesAdapter.submitList(candidates) // Met à jour la liste dans l'adapteur
                binding.loading.visibility = View.GONE // Masque le loader
            }
        }
    }

    // RecyclerView affichant la liste des candidats
    private fun setRecyclerView() {
        candidatesAdapter = CandidatesAdapter { item ->
            val intent = CandidateDetailActivity.createIntent(requireContext(), item.id)
            println(item.picture)
            detailLauncher.launch(intent)
        }
        binding.recyclerviewCandidate.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewCandidate.adapter = candidatesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}