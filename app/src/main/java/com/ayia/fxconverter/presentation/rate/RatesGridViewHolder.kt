package com.ayia.fxconverter.presentation.rate


import androidx.recyclerview.widget.RecyclerView
import com.ayia.fxconverter.databinding.RowRateBinding
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.presentation.main.MainViewModel

class RatesGridViewHolder (val binding: RowRateBinding) : RecyclerView.ViewHolder(binding.root) {

    val foreground = binding.layoutForeground

    fun bind(position: Int, rate: Rate, clickCallback: RateClickCallback, viewModel: MainViewModel) {
        binding.vm = viewModel
        binding.position = position
        binding.rate = rate
        binding.clickCallBack = clickCallback
        binding.executePendingBindings()
    }


}