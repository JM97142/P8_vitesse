package com.example.p8_vitesse.ui.candidateDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.p8_vitesse.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CandidateDetailActivity: AppCompatActivity() {
    companion object {
        const val EXTRA_CANDIDATE_ID = "extra_candidate_id"
        fun createIntent(context: Context, candidateId: Long): Intent =
            Intent(context, CandidateDetailActivity::class.java)
                .putExtra(EXTRA_CANDIDATE_ID, candidateId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate_details)

        val id = intent?.getLongExtra(EXTRA_CANDIDATE_ID, -1L) ?: -1L
        // TODO: charger les détails du candidat via ViewModel
    }
}