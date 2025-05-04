package com.example.p8_vitesse.ui.candidateDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.p8_vitesse.R
import com.example.p8_vitesse.domain.model.Items
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CandidateDetailActivity: AppCompatActivity() {
    private val viewModel: CandidateDetailViewModel by viewModels()

    companion object {
        const val EXTRA_CANDIDATE_ID = "extra_candidate_id"
        fun createIntent(context: Context, candidateId: Long): Intent =
            Intent(context, CandidateDetailActivity::class.java)
                .putExtra(EXTRA_CANDIDATE_ID, candidateId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)
        setBack()
        viewModel.candidate.observe(this) { candidate ->
            candidate?.let { bindCandidateData(it) }
        }
    }

    private fun setBack() {
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun bindCandidateData(candidate: Items) {
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        val eurosSalary = findViewById<TextView>(R.id.eurosSalary)
        val notes = findViewById<TextView>(R.id.notesValue)

        viewModel.candidate.observe(this) { candidate ->
            candidate?.let {
                toolbar.title = "${it.firstName} ${it.lastName}"
                eurosSalary.text = "${it.wage} €"
                notes.text = it.note

                // TODO: Glide.with(this).load(it.imageUrl).into(imageView)
            }
        }
    }
}