package com.example.p8_vitesse.ui.editCandidate

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.p8_vitesse.R
import com.example.p8_vitesse.databinding.ActivityEditCandidateBinding
import com.example.p8_vitesse.domain.model.Items
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCandidateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCandidateBinding
    private val viewModel: EditCandidateViewModel by viewModels()

    companion object {
        private const val EXTRA_CANDIDATE_ID = "extra_candidate_id"

        fun createIntent(context: Context, candidateId: Long): Intent {
            return Intent(context, EditCandidateActivity::class.java).apply {
                putExtra(EXTRA_CANDIDATE_ID, candidateId)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditCandidateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        val candidateId = intent.getLongExtra(EXTRA_CANDIDATE_ID, -1L)
        if (candidateId != -1L) {
            viewModel.loadCandidate(candidateId)
        }

        viewModel.candidate.observe(this) { candidate ->
            candidate?.let { fillFormWithCandidateData(it) }
        }

        binding.filledButton.setOnClickListener {
            val updatedCandidate = collectCandidateFromForm()
            viewModel.updateCandidate(updatedCandidate)
            finish()
        }
    }

    private fun setToolbar() {
        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun fillFormWithCandidateData(candidate: Items) {
        binding.firstName.setText(candidate.firstName)
        binding.lastName.setText(candidate.lastName)
        binding.salary.setText(candidate.wage.toString())
        binding.notes.setText(candidate.note)
    }

    private fun collectCandidateFromForm(): Items {
        return Items(
            id = viewModel.candidate.value!!.id,
            firstName = binding.firstName.text.toString(),
            lastName = binding.lastName.text.toString(),
            note = binding.notes.text.toString(),
            wage = binding.salary.text.toString().toDouble(),
            phone = binding.phone.text.toString(),
            email = binding.email.text.toString(),
            birthday = binding.date.text.toString(),
            picture = viewModel.candidate.value!!.picture,
            favorite = false
        )
    }
}