package com.example.p8_vitesse.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.example.p8_vitesse.R
import com.example.p8_vitesse.databinding.ItemBinding
import com.example.p8_vitesse.domain.model.Items

class CandidatesAdapter(
    private val onItemClick: (Items) -> Unit
): ListAdapter<Items, CandidatesAdapter.CandidateViewHolder>(
        DIFF_CALLBACK
    ) {
    // ViewHolder qui gère l'affichage d'un item
    inner class CandidateViewHolder(private val binding: ItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            @SuppressLint("SetTextI18n")
            fun bind(items: Items) {
                binding.tvName.text = "${items.firstName} ${items.lastName.uppercase()}"
                binding.tvDescription.text = items.note

                if (!items.picture.isNullOrEmpty()) {
                    Glide.with(binding.imgCandidate.context)
                        .load(items.picture) // Image chargée
                        .placeholder(R.drawable.image_placeholder) // une image par défaut
                        .into(binding.imgCandidate)
                } else {
                    binding.imgCandidate.setImageResource(R.drawable.image_placeholder)
                }

                // Gestion du clic sur l'item
                binding.root.setOnClickListener {
                    onItemClick(items)
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CandidateViewHolder {
        val binding = ItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CandidateViewHolder(binding)
    }

    // Lie un item à son ViewHolder
    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    // Détecte les changements dans la liste
    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Items> =
            object : DiffUtil.ItemCallback<Items>() {
                override fun areItemsTheSame(old: Items, new: Items) = old.id == new.id
                override fun areContentsTheSame(old: Items, new: Items): Boolean = old == new
            }
    }
}