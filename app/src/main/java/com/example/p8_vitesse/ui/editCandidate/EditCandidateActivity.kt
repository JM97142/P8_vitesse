package com.example.p8_vitesse.ui.editCandidate

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.p8_vitesse.databinding.ActivityEditCandidateBinding
import com.example.p8_vitesse.domain.model.Items
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class EditCandidateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditCandidateBinding
    private val viewModel: EditCandidateViewModel by viewModels()

    private var imageUri: Uri? = null
    // Lanceur pour sélectionner une image
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            binding.imgUpdate.setImageURI(it)
        }
    }

    companion object {
        private const val EXTRA_CANDIDATE_ID = "extra_candidate_id"
        // Méthode pour créer un Intent vers cette activité avec l'ID candidat
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
        setToolbar() // Barre supérieur

        // Ouvre la galerie pour choisir une image
        binding.imgUpdate.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        binding.date.setOnClickListener {
            showDatePicker()
        }

        // Charge les données du candidat via ViewModel
        val candidateId = intent.getLongExtra(EXTRA_CANDIDATE_ID, -1L)
        if (candidateId != -1L) {
            viewModel.loadCandidate(candidateId)
        }

        // Remplit le formulaire avec les données reçues
        viewModel.candidate.observe(this) { candidate ->
            candidate?.let { fillFormWithCandidateData(it) }
        }

        binding.filledButton.setOnClickListener {
            val updatedCandidate = collectCandidateFromForm()
            if (updatedCandidate != null) {
                viewModel.updateCandidate(updatedCandidate)
                Toast.makeText(this, "Candidat mis à jour avec succès", Toast.LENGTH_SHORT).show()
                finish()
            }

        }
    }

    private fun setToolbar() {
        binding.topAppBar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    // Remplit tous les champs du formulaire avec les données du candidat
    @SuppressLint("SetTextI18n")
    private fun fillFormWithCandidateData(candidate: Items) {
        binding.firstName.setText(candidate.firstName)
        binding.lastName.setText(candidate.lastName)
        binding.date.setText(candidate.birthday)
        binding.email.setText(candidate.email)
        binding.phone.setText(candidate.phone)
        binding.salary.setText(candidate.wage.toString())
        binding.notes.setText(candidate.note)

        // Charger image existante
        if (!candidate.picture.isNullOrEmpty()) {
            imageUri = Uri.parse(candidate.picture)
            Glide.with(this)
                .load(candidate.picture)
                .into(binding.imgUpdate)
        }
    }

    private fun collectCandidateFromForm(): Items? {
        val candidate = viewModel.candidate.value
        if (candidate == null) {
            Toast.makeText(this, "Candidat non chargé", Toast.LENGTH_SHORT).show()
            return null
        }

        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()
        val phone = binding.phone.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val date = binding.date.text.toString().trim()
        val salary = binding.salary.text.toString().trim()
        val notes = binding.notes.text.toString().trim()

        if (firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() ||
            email.isEmpty() || date.isEmpty() || salary.isEmpty() || notes.isEmpty()
        ) {
            Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT).show()
            return null
        }

        // Salaire
        val wageDouble = salary.toDoubleOrNull()
        if (wageDouble == null) {
            Toast.makeText(this, "Le salaire est invalide", Toast.LENGTH_SHORT).show()
            return null
        }

        // Retourne une copie mise à jour du candidat
        return candidate.copy(
            firstName = firstName,
            lastName = lastName,
            phone = phone,
            email = email,
            birthday = date,
            wage = wageDouble,
            note = notes,
            picture = imageUri?.toString() ?: candidate.picture
        )
    }

    // Configure et affiche le DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.date.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePicker.show()
    }
}