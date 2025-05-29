package com.example.p8_vitesse.ui.candidateDetails

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.p8_vitesse.R
import com.example.p8_vitesse.databinding.ActivityCandidateDetailsBinding
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.editCandidate.EditCandidateActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
        // Méthode pour créer l'intent avec un ID de candidat
        fun createIntent(context: Context, candidateId: Long): Intent =
            Intent(context, CandidateDetailActivity::class.java)
                .putExtra(EXTRA_CANDIDATE_ID, candidateId)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCandidateDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setBack() // Bouton de retour
        setDeleteButton() // Bouton de supression
        setEditButton() // Bouton de modification
        setFavoriteButton() // Bouton favoris

        // Collecte du StateFlow dans une coroutine lifecycle-aware
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.candidate.collect { candidate ->
                    candidate?.let { bindCandidateData(it) }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.conversionRate.collect { rate ->
                    val candidate = viewModel.candidate.value
                    if (rate != null && candidate != null) {
                        val pounds = candidate.wage * rate
                        binding.poundSalary.text = "soit £ %.2f".format(pounds)
                    }
                }
            }
        }
    }

    // Recharge les infos du candidat depuis la BDD après modification
    override fun onResume() {
        super.onResume()
        viewModel.refreshCandidate()
    }

    // Gère le bouton retour de la toolbar
    private fun setBack() {
        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

    }

    // Remplit les vues avec les infos du candidat
    @SuppressLint("SetTextI18n")
    private fun bindCandidateData(candidate: Items) {
        binding.topAppBar.title = "${candidate.firstName} ${candidate.lastName}"
        binding.eurosSalary.text = "${candidate.wage} €"
        binding.notesValue.text = candidate.note

        // Format date + âge
        candidate.birthday?.let {
            val formatted = formatBirthday(it)
            binding.aboutAge.text = formatted
        }

        // Charge l’image avec Glide
        if (!candidate.picture.isNullOrEmpty()) {
            Glide.with(this)
                .load(candidate.picture)
                .placeholder(R.drawable.image_placeholder) // image par défaut si null
                .into(binding.imgCandidate)
        }

        // Action bouton appel
        binding.callIconButton.setOnClickListener {
            candidate.phone?.let { phoneNumber ->
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = android.net.Uri.parse("tel:$phoneNumber")
                }
                startActivity(intent)
            }
        }

        // Action bouton SMS
        binding.smsIconButton.setOnClickListener {
            candidate.phone?.let { phoneNumber ->
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = android.net.Uri.parse("sms:$phoneNumber")
                }
                startActivity(intent)
            }
        }

        // Action bouton email
        binding.emailIconButton.setOnClickListener {
            candidate.email?.let { email ->
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = android.net.Uri.parse("mailto:$email")
                }
                startActivity(intent)
            }
        }

    }

    // Formate la date de naissance et calcule l'âge
    private fun formatBirthday(birthday: String): String {
        return try {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val birthDate = LocalDate.parse(birthday, formatter)
            val age = Period.between(birthDate, LocalDate.now()).years
            "${birthDate.format(formatter)} ($age ans)"
        } catch (e: Exception) {
            "Date invalide"
        }
    }

    // Configuration bouton de suppression
    private fun setDeleteButton() {
        binding.topAppBar.menu.findItem(R.id.ic_delete).setOnMenuItemClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Suppression")
                .setMessage("Etes-vous sur de vouloir supprimer ce candidat ? Cette action est irréversible")
                .setNegativeButton("Annuler") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Confirmer") { _, _ ->
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
    // Configuration du bouton d'édition
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
    // Configuration bouton ajout en favoris
    private fun setFavoriteButton() {
        val favoriteItem = binding.topAppBar.menu.findItem(R.id.ic_favorite)
        // Mise à jour de l'icône
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.candidate.collect { candidate ->
                    candidate?.let {
                        favoriteItem.setIcon(
                            if (it.favorite) R.drawable.star_inline
                            else R.drawable.star_outline
                        )
                    }
                }
            }
        }
        // Clique sur l’icône : inverse l'état du favori
        favoriteItem.setOnMenuItemClickListener {
            viewModel.toggleFavorite()
            true
        }
    }

}