package com.ayia.fxconverter.presentation.options

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayia.fxconverter.databinding.RowOptionBinding
import com.ayia.fxconverter.domain.models.Option
import com.ayia.fxconverter.util.BASE_TAG

class OptionsAdapter(private val clickCallback: OptionClickCallback) :
    ListAdapter<Option, OptionsAdapter.ViewHolder>(OptionDiffCallback()), Filterable {


    private val myTag: String = BASE_TAG + " " + OptionsAdapter::class.java.simpleName

    private var _list: MutableList<Option>? = null
    private var _filteredList: MutableList<Option>? = null

    fun modifyList(list: MutableList<Option>?) {
        _list = list
        _filteredList = list
        submitList(list)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.bind(item, clickCallback)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: RowOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(option: Option, clickCallback: OptionClickCallback) {
            binding.option = option
            binding.clickCallback = clickCallback
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowOptionBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {

                val charString = charSequence.toString()

                _filteredList = if (charString.isEmpty()) {
                    _list
                } else {

                    val resultList = mutableListOf<Option>()

                    _list?.let { list ->
                        for (row in list) {
                            if (
                                row.name.lowercase().contains(charString.lowercase()
                                    .trim { it <= ' ' }) ||
                                row.name.lowercase().contains(charString.lowercase()
                                    .trim { it <= ' ' })
                            ) {
                                resultList.add(row)
                            }
                        }
                    }
                    resultList
                }

                val filterResults = FilterResults()
                filterResults.values = _filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                _filteredList = filterResults.values as MutableList<Option>
                submitList(_filteredList)
            }
        }
    }


}
