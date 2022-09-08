package com.ayia.fxconverter.presentation.rate

import androidx.recyclerview.widget.DiffUtil
import com.ayia.fxconverter.domain.models.Rate

class RateDiffCallback : DiffUtil.ItemCallback<Rate>() {
    override fun areItemsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Rate, newItem: Rate): Boolean {
        return oldItem.code == newItem.code && oldItem.amount == newItem.amount
    }
}