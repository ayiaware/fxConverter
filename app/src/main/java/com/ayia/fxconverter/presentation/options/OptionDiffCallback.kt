package com.ayia.fxconverter.presentation.options

import androidx.recyclerview.widget.DiffUtil
import com.ayia.fxconverter.domain.models.Option

class OptionDiffCallback : DiffUtil.ItemCallback<Option>() {
    override fun areItemsTheSame(oldItem: Option, newItem: Option): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Option, newItem: Option): Boolean {
        return oldItem.name == newItem.name && oldItem.value == newItem.value
    }
}