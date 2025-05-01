package com.example.p8_vitesse.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.p8_vitesse.databinding.ItemBinding
import com.example.p8_vitesse.domain.model.Items

class CandidatesAdapter(
    private val onItemClick: (Items) -> Unit
): ListAdapter<Items, CandidatesAdapter.CandidateViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class CandidateViewHolder(private val binding: ItemBinding):
        RecyclerView.ViewHolder(binding.root) {
            fun bind(items: Items) {
                binding.tvName.text = "${items.firstName} ${items.lastName.uppercase()}"
                binding.tvDescription.text = items.note

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

    override fun onBindViewHolder(holder: CandidateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Items> =
            object : DiffUtil.ItemCallback<Items>() {
                override fun areItemsTheSame(old: Items, new: Items) = old.id == new.id
                override fun areContentsTheSame(old: Items, new: Items): Boolean = old == new
            }
    }
}