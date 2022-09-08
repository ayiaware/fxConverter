package com.ayia.fxconverter.presentation.rate

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayia.fxconverter.databinding.RowRateBinding
import com.ayia.fxconverter.databinding.RowRateLinearBinding
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.presentation.main.MainViewModel
import com.ayia.fxconverter.util.BASE_TAG

class RatesAdapter(
    private val clickCallback: RateClickCallback,
    private val viewModel: MainViewModel
) :
    ListAdapter<Rate, RecyclerView.ViewHolder>(RateDiffCallback()) {


    private val myTag: String = BASE_TAG + " " + RatesAdapter::class.java.simpleName

    private var list: List<Rate>? = null

    private var selectedIdsSBArray: SparseBooleanArray? = null

    private var ratesViewType : RatesViewType = RatesViewType.GRID


     fun setMyViewType(viewType: RatesViewType){
        this.ratesViewType = viewType
     }

    fun  getRatesViewType(): RatesViewType{
        return ratesViewType
    }


    override fun submitList(list: List<Rate>?) {

        this.list = list
        selectedIdsSBArray = SparseBooleanArray()

        super.submitList(list)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)

        when (getItemViewType(position)) {
            RatesViewType.GRID.ordinal -> {
                if (holder is RatesGridViewHolder)
                {
                    holder.bind(position, item, clickCallback, viewModel)
                    holder.foreground.visibility =
                        if (selectedIdsSBArray!![position]) View.VISIBLE else View.GONE
                }

            }
            else -> {
                if (holder is RatesLinearViewHolder)
                {
                    holder.bind(position, item, clickCallback, viewModel)
                    holder.foreground.visibility =
                        if (selectedIdsSBArray!![position]) View.VISIBLE else View.GONE
                }
            }
        }




    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            RatesViewType.GRID.ordinal -> {
                RatesGridViewHolder(
                    RowRateBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                RatesLinearViewHolder(
                    RowRateLinearBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return ratesViewType.ordinal
    }

    fun setViewType(ratesViewType: RatesViewType){

    }


    /***
     * Methods required for do selections, remove selections, etc.
     */
    //Toggle selection methods
    fun toggleSelection(position: Int) {
        selectView(position)
        notifyDataSetChanged()

    }


    //Put or delete selected position into SparseBooleanArray
    private fun selectView(position: Int) {
        if (selectedIdsSBArray!![position, false]) {
            selectedIdsSBArray!!.delete(position)
        } else {
            selectedIdsSBArray!!.put(position, true)
        }
        notifyItemChanged(position)
    }

    fun removeSelections() {
        selectedIdsSBArray!!.clear()
        notifyDataSetChanged()
    }


    //Return all selected ids
    fun getSelectedIds(): Array<String> {

        val ids = arrayOf<String>()

        for (i in 0 until selectedItemCount) {
            ids[i] = list!![selectedIdsSBArray!!.keyAt(i)].code
        }

        return ids
    }

    //Get total selected count
    private val selectedItemCount: Int
        get() = selectedIdsSBArray!!.size()

    fun getSelectedRates(): List<Rate> {

        val items: MutableList<Rate> = ArrayList(selectedItemCount)

        for (i in 0 until selectedItemCount) {
            items.add(list!![selectedIdsSBArray!!.keyAt(i)])
        }
        return items
    }


}
