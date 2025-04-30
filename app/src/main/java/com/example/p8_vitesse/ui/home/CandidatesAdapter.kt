package com.example.p8_vitesse.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import com.example.p8_vitesse.databinding.ItemBinding
import com.example.p8_vitesse.domain.model.Items
import com.example.p8_vitesse.ui.home.allCandidates.AllCandidatesFragment

class CandidatesAdapter(allCandidatesFragment: AllCandidatesFragment) :
    ListAdapter<Items, CandidatesAdapter.CandidateViewHolder>(
        DIFF_CALLBACK
    ) {

    inner class CandidateViewHolder(binding: ItemBinding): RecyclerView.ViewHolder(binding.root) {
        var candidateName: TextView = binding.tvName
        var candidateDescription: TextView = binding.tvDescription
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
        val item = getItem(position)

        holder.candidateName.text = item.firstName + " " + item.lastName.uppercase()
        holder.candidateDescription.text = item.note
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<Items> =
            object : DiffUtil.ItemCallback<Items>() {
                override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
                    return oldItem === newItem
                }

                override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
                    return oldItem == newItem
                }
            }
    }
}