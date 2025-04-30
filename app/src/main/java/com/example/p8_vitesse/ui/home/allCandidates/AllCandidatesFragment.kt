package com.example.p8_vitesse.ui.home.allCandidates

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.p8_vitesse.databinding.RecyclerCandidatesBinding
import com.example.p8_vitesse.ui.home.CandidatesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllCandidatesFragment: Fragment() {
    private var _binding: RecyclerCandidatesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AllCandidatesViewModel by viewModels()
    private lateinit var candidatesAdapter: CandidatesAdapter

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
        binding.loading.visibility = View.VISIBLE

        setRecyclerView()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.candidates.collect { candidates ->
                candidatesAdapter.submitList(candidates)
                binding.loading.visibility = View.GONE
            }
        }
    }

    private fun setRecyclerView() {
        candidatesAdapter = CandidatesAdapter(this)
        binding.recyclerviewCandidate.layoutManager = LinearLayoutManager(context)
        binding.recyclerviewCandidate.adapter = candidatesAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}