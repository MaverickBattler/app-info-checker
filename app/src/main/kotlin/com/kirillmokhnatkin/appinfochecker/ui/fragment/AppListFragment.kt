package com.kirillmokhnatkin.appinfochecker.ui.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.kirillmokhnatkin.appinfochecker.R
import com.kirillmokhnatkin.appinfochecker.databinding.FragmentAppListBinding
import com.kirillmokhnatkin.appinfochecker.flow_util.collectValues
import com.kirillmokhnatkin.appinfochecker.ui.adapter.AppListAdapter
import com.kirillmokhnatkin.appinfochecker.ui.state.AppListUiState
import com.kirillmokhnatkin.appinfochecker.ui.viewmodel.AppListViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AppListFragment : Fragment() {

    private var _binding: FragmentAppListBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<AppListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupAppList()
        binding.swiperefresh.setOnRefreshListener {
            viewModel.refreshAppList()
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private var recentlyClicked = false // for debounce effect on click
        set(value) {
            field = value
            Handler(Looper.getMainLooper()).postDelayed({ field = false }, 500)
        }

    private fun setupAppList() {
        val onItemClickListener = { packageName: String ->
            if (!recentlyClicked) {
                recentlyClicked = true
                val action = AppListFragmentDirections.actionAppListToAppInfo(packageName)
                findNavController().navigate(action)
            }
        }
        val adapter = AppListAdapter(onItemClickListener)
        viewModel.uiState.collectValues(lifecycleScope, viewLifecycleOwner.lifecycle) { uiState ->
            when (uiState) {
                is AppListUiState.Content -> {
                    if (uiState.shouldHideRefreshLayoutIndicator) {
                        binding.swiperefresh.isRefreshing = false
                    }
                    binding.progressBar.isVisible = false
                    adapter.submitList(uiState.appsInfoList)
                }

                AppListUiState.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar)
        toolbar.setOnClickListener {
            binding.appListRecyclerview.scrollToPosition(0)
        }
        binding.appListRecyclerview.apply {
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            this.adapter = adapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onDestroyView() {
        _binding = null
        requireActivity().findViewById<Toolbar>(R.id.toolbar).setOnClickListener(null)
        super.onDestroyView()
    }
}