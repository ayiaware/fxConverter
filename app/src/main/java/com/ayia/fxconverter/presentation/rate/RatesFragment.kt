package com.ayia.fxconverter.presentation.rate

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayia.fxconverter.R
import com.ayia.fxconverter.data.Resource
import com.ayia.fxconverter.databinding.FragmentRatesBinding
import com.ayia.fxconverter.domain.models.Rate
import com.ayia.fxconverter.presentation.main.MainViewModel
import com.ayia.fxconverter.util.BASE_TAG
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class RatesFragment : Fragment() {

    private var _binding: FragmentRatesBinding? = null

    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var adapter: RatesAdapter

    private val TAG = BASE_TAG + " " + RatesFragment::class.simpleName

    private val KEY_LAYOUT_MANAGER = "layoutManager"

    private val KEY_SCROLL_POSITION = "scrollPosition"
    private var scrollPosition = 0

    private var ratesViewType: RatesViewType? = RatesViewType.GRID
    private var layoutManager: RecyclerView.LayoutManager? = null


    //https://medium.com/androiddevelopers/a-safer-way-to-collect-flows-from-android-uis-23080b1f8bda


    private var rvScrollJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRatesBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(savedInstanceState)

        mainViewModel.infoAndRatesObs.observe(viewLifecycleOwner) { rates ->

            if (rates.isNotEmpty()) {
                Timber.tag(TAG).d("ratesSize ${rates.size}")
                adapter.submitList(rates)
                binding.isEmptyLayout.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                binding.progressbar.visibility = View.GONE
            }

        }

        viewLifecycleOwner.lifecycleScope.launch {

            //Only use for network check

//            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
//
//
//            }

            mainViewModel.infoAndRatesEvent.collect { event ->

                Timber.tag(TAG).d("collect")

                when (event) {

                    is Resource.Empty -> {
                        Timber.tag(TAG).d("Empty")
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.isEmptyLayout.visibility = View.VISIBLE
                        binding.progressbar.visibility = View.GONE
                        binding.tvMsg.text = getText(event.txtId)
                    }
                    is Resource.Error -> {
                        Timber.tag(TAG).d("Error")
                        binding.progressbar.visibility = View.GONE
                        binding.tvMsg.text = getText(event.txtId)

                        Snackbar.make(view, event.txtId, Snackbar.LENGTH_LONG)
                            .setAction(
                                "Retry"
                            ) {
                                mainViewModel.refreshInfoAndRatesObs(false)
                            }.show()
                    }
                    is Resource.Loading -> {
                        Timber.tag(TAG).d("Loading")
                        binding.recyclerView.visibility = View.INVISIBLE
                        binding.isEmptyLayout.visibility = View.GONE
                        binding.progressbar.visibility = View.VISIBLE
                    }
                    else -> {
                        Timber.tag(TAG).d("Success")
                    }

                }
            }

        }

        binding.btnError.setOnClickListener {

            mainViewModel.refreshInfoAndRatesObs(true)
        }

        var searchJob: Job? = null

        binding.etSearch.addTextChangedListener { text ->
            searchJob?.cancel()

            searchJob = MainScope().launch {

                delay(500L)

                mainViewModel.setSearchRatesQuery(text.toString())

            }
        }

        binding.tilSearch.setEndIconOnClickListener {
            binding.etSearch.text?.clear()
            binding.etSearch.clearFocus()
            binding.switchLayout.visibility = View.VISIBLE
        }

        binding.etSearch.setOnFocusChangeListener { _, hasFocus ->

            if (hasFocus) {
                binding.switchLayout.visibility = View.GONE
                binding.tilSearch.isEndIconVisible = true
            } else {
                binding.switchLayout.visibility = View.VISIBLE
                binding.tilSearch.isEndIconVisible = false
            }

        }

        binding.switchLayout.setOnClickListener {

            if (adapter.getRatesViewType() == RatesViewType.GRID) {

                setRecyclerViewLayoutManager(RatesViewType.LINEAR)

                binding.switchLayout.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_grid_view
                    )
                )
            } else {
                setRecyclerViewLayoutManager(RatesViewType.GRID)
                binding.switchLayout.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_linear_view
                    )
                )

            }

        }


    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_LAYOUT_MANAGER, ratesViewType)
        outState.putInt(KEY_SCROLL_POSITION, scrollPosition)
    }

    private fun setupRecyclerView(savedInstanceState: Bundle?) {

        adapter = RatesAdapter(clickCallback, mainViewModel)

        binding.recyclerView.adapter = adapter

        if (savedInstanceState != null) {
            // Restore saved layout manager type.
            ratesViewType = savedInstanceState.getSerializable(KEY_LAYOUT_MANAGER) as RatesViewType
            scrollPosition = savedInstanceState.getInt(KEY_SCROLL_POSITION)

        }
        setRecyclerViewLayoutManager(ratesViewType)
    }

    private fun setRecyclerViewLayoutManager(ratesViewType: RatesViewType?) {

        this.ratesViewType = ratesViewType
        adapter.setMyViewType(ratesViewType!!)


        // If a layout manager has already been set, get current scroll position.
        if (binding.recyclerView.layoutManager != null) {
            scrollPosition = (binding.recyclerView.layoutManager as LinearLayoutManager)
                .findFirstCompletelyVisibleItemPosition()
        }
        layoutManager = when (ratesViewType) {
            RatesViewType.GRID -> {
                GridLayoutManager(activity, 3)

            }
            RatesViewType.LINEAR -> {
                LinearLayoutManager(activity)
            }
        }
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.scrollToPosition(scrollPosition)
    }

    private val clickCallback: RateClickCallback = object : RateClickCallback {
        override fun onClick(rate: Rate, position: Int) {

            showRateDetails(rate)
        }

    }

    private fun showRateDetails(rate: Rate) {

        val tag = RateFragment::class.java.simpleName
        val ft = requireActivity().supportFragmentManager.beginTransaction()
        val prev = requireActivity().supportFragmentManager.findFragmentByTag(tag)

        if (prev == null) {
            val fragment: BottomSheetDialogFragment =
                RateFragment.newInstance(rate.code, rate.infoBaseCode)
            fragment.show(ft, tag)
        }

    }

    private fun shrinkExpandFab(isShrink: Boolean) {
        Timber.tag(BASE_TAG).d("shrinkFab $isShrink")
        rvScrollJob?.cancel()

        rvScrollJob = lifecycleScope.launch {
            setShrinkFab(isShrink)
        }
    }

    private suspend fun setShrinkFab(isShrink: Boolean) {
        Timber.tag(BASE_TAG).d("setShrinkFab $isShrink")
        delay(200L)
        mainViewModel.shrinkFab.postValue(isShrink)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}