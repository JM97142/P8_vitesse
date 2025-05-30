package com.example.p8_vitesse.ui.addCandidate

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.p8_vitesse.R
import com.example.p8_vitesse.domain.model.Items
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@AndroidEntryPoint
class AddCandidateActivity: AppCompatActivity() {

    private val viewModel: AddCandidatViewModel by viewModels()

    private lateinit var candidateImage: ImageView
    private var imageUri: Uri? = null

    // Lanceur pour sélectionner une image
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            candidateImage.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_candidate)

        setToolbar() // Barre supérieure
        setForm() // Formulaire
    }

    // Configuration de bouton retour en arrière
    private fun setToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    // Initialise les composants du formulaire
    private fun setForm() {
        candidateImage = findViewById<ImageView>(R.id.img_add)
        val firstNameField = findViewById<TextInputEditText>(R.id.first_name)
        val lastNameField = findViewById<TextInputEditText>(R.id.last_name)
        val phoneField = findViewById<TextInputEditText>(R.id.phone)
        val emailField = findViewById<TextInputEditText>(R.id.email)
        val dateField = findViewById<TextInputEditText>(R.id.date)
        val salaryField = findViewById<TextInputEditText>(R.id.salary)
        val notesField = findViewById<TextInputEditText>(R.id.notes)
        val saveButton = findViewById<Button>(R.id.filledButton)

        // Ouvre la galerie pour choisir une image
        candidateImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // Configuration du sélecteur de date
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        dateField.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    dateField.setText(dateFormat.format(calendar.time))
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }

        // Configuration de la sauvegarde d'un candidat
        saveButton.setOnClickListener {
            // Récupère les valeurs des champs de saisie
            val firstName = firstNameField.text.toString().trim()
            val lastName = lastNameField.text.toString().trim()
            val phone = phoneField.text.toString().trim()
            val email = emailField.text.toString().trim()
            val date = dateField.text.toString().trim()
            val salary = salaryField.text.toString().trim()
            val notes = notesField.text.toString().trim()

            when {
                firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() ||
                        date.isEmpty() || salary.isEmpty() || notes.isEmpty() -> {
                    Toast.makeText(this, "Tous les champs doivent être remplis", Toast.LENGTH_SHORT)
                        .show()
                }

                !isValidEmail(email) -> {
                    emailField.error = "Email invalide"
                }

                salary.toDoubleOrNull() == null -> {
                    salaryField.error = "Salaire invalide"
                }

                else -> {
                    // Création d'un objet Items et l’envoie au ViewModel
                    val newCandidate = Items(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        email = email,
                        birthday = date,
                        wage = salary.toDoubleOrNull()
                            ?: 0.0,
                        note = notes,
                        picture = imageUri?.toString(),
                        favorite = false
                    )
                    viewModel.addCandidate(newCandidate)

                    Toast.makeText(this, "Candidat ajouté avec succès", Toast.LENGTH_SHORT).show()

                    finish()
                }
            }
        }
    }

    // Vérification email format
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}