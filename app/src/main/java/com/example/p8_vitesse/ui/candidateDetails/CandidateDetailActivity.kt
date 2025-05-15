package com.example.p8_vitesse.ui.candidateDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.p8_vitesse.R
import com.example.p8_vitesse.databinding.ActivityCandidateDetailsBinding
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.editCandidate.EditCandidateActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CandidateDetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityCandidateDetailsBinding
    private val viewModel: CandidateDetailViewModel by viewModels()

    companion object {
        const val EXTRA_CANDIDATE_ID = "extra_candidate_id"
        const val EXTRA_DELETED_ID = "extra_deleted_id"

        fun createIntent(context: Context, candidateId: Long): Intent =
            Intent(context, CandidateDetailActivity::class.java)
                .putExtra(EXTRA_CANDIDATE_ID, candidateId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBack()
        setDeleteButton()
        setEditButton()
        setFavoriteButton()

        viewModel.candidate.observe(this) { candidate ->
            candidate?.let {
                bindCandidateData(it)
            }
        }
    }

    private fun setBack() {
        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

    }

    @SuppressLint("SetTextI18n")
    private fun bindCandidateData(candidate: Items) {
        viewModel.candidate.observe(this) { candidate ->
            candidate?.let {
                binding.topAppBar.title = "${it.firstName} ${it.lastName}"
                // binding.aboutAge.text = formatBirthday(it.birthday)
                binding.eurosSalary.text = "${it.wage} €"
                binding.poundSalary.text = "soit £ ${convertToPound(it.wage)}"
                binding.notesValue.text = it.note

                // TODO: Glide.with(this).load(it.imageUrl).into(imageView)
            }
        }
    }

    /* private fun formatBirthday(birthDate: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val age = Period.between(birthDate, LocalDate.now()).years
        return "${birthDate.format(formatter)} ($age ans)"
    }*/

    @SuppressLint("DefaultLocale")
    private fun convertToPound(euros: Double): String {
        val rate = 0.865
        return String.format("%.2f", euros * rate)
    }

    private fun setDeleteButton() {
        binding.topAppBar.menu.findItem(R.id.ic_delete).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Supprimer ?")
                .setMessage("Test")
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Supprimer") { _, _ ->
                    val candidateId = intent.getLongExtra(EXTRA_CANDIDATE_ID, -1L)
                    if (candidateId != -1L) {
                        viewModel.deleteCandidate(candidateId)
                        // Retourner l'ID supprimé au parent
                        val resultIntent = Intent().apply {
                            putExtra(EXTRA_DELETED_ID, candidateId)
                        }
                        setResult(RESULT_OK, resultIntent)
                    }
                    finish()
                }
                .show()
            true
        }
    }

    private fun setEditButton() {
        binding.topAppBar.menu.findItem(R.id.ic_edit).setOnMenuItemClickListener {
            val candidateId = intent.getLongExtra(EXTRA_CANDIDATE_ID, -1L)
            if (candidateId != -1L) {
                val intent = EditCandidateActivity.createIntent(this, candidateId)
                startActivity(intent)
            }
            true
        }
    }

    private fun setFavoriteButton() {
        val favoriteItem = binding.topAppBar.menu.findItem(R.id.ic_favorite)

        viewModel.candidate.observe(this) { candidate ->
            if (candidate != null) {
                favoriteItem.setIcon(
                    if (candidate.favorite) R.drawable.star_inline
                    else R.drawable.star_outline
                )
            }
        }

        favoriteItem.setOnMenuItemClickListener {
            viewModel.toggleFavorite()
            true
        }
    }

}