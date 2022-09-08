package com.ayia.fxconverter.presentation.rate

import androidx.recyclerview.widget.RecyclerView
import com.ayia.fxconverter.databinding.RowRateLinearBinding
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.presentation.main.MainViewModel

class RatesLinearViewHolder (val binding: RowRateLinearBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val foreground = binding.layoutForeground

    fun bind(position: Int, rate: Rate, clickCallback: RateClickCallback, viewModel: MainViewModel) {

        binding.vm = viewModel
        binding.position = position
        binding.rate = rate
        binding.clickCallBack = clickCallback
        binding.executePendingBindings()
    }


}