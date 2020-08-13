package com.shadi.feature.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.shadi.R
import com.shadi.databinding.FragmentMatchMakingBinding
import com.shadi.feature.adapter.MatchListAdapter
import com.shadi.feature.viewModel.MatchMakingViewModel
import com.shadi.paging.pagingAdapter.ReposLoadStateAdapter
import com.shadi.util.showToast
import com.shadi.util.toVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MatchMakingFragment : Fragment(R.layout.fragment_match_making) {

    private val matchMakingViewModel: MatchMakingViewModel by viewModels()
    private var job: Job? = null

    private val adapter = MatchListAdapter {
        lifecycleScope.launch {
            matchMakingViewModel.updateMatch(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mBinding = FragmentMatchMakingBinding.bind(view)
        initMatchListAdapter(mBinding)
        setUpSwipeToRefresh(mBinding)
        getMatch()
    }

    private fun setUpSwipeToRefresh(mBinding: FragmentMatchMakingBinding) {
        mBinding.swipeRefreshLayout.setOnRefreshListener {
            adapter.refresh()
            mBinding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun initMatchListAdapter(mBinding: FragmentMatchMakingBinding) {
        with(mBinding) {
            rvMatch.adapter = adapter.withLoadStateFooter(
                footer = ReposLoadStateAdapter { adapter.retry() }
            )
            adapter.addLoadStateListener { loadState ->
                if (loadState.source.refresh !is LoadState.NotLoading) {
                    progressBar.visibility = toVisibility(loadState.refresh is LoadState.Loading)
                    btnRetry.visibility = toVisibility(loadState.refresh is LoadState.Error)
                } else {
                    progressBar.visibility = View.GONE
                    btnRetry.visibility = View.GONE
                    swipeRefreshLayout.isRefreshing = false
                    val errorState = when {
                        loadState.append is LoadState.Error -> {
                            loadState.append as LoadState.Error
                        }
                        loadState.prepend is LoadState.Error -> {
                            loadState.prepend as LoadState.Error
                        }
                        else -> {
                            null
                        }
                    }
                    errorState?.let {
                        if(it.error.toString().contains("Remote key").not())
                        requireContext().showToast(it.error.toString())
                    }
                }
            }
        }
    }

    private fun getMatch() {
        job?.cancel()
        job = lifecycleScope.launch {
            matchMakingViewModel.matchList.collectLatest { adapter.submitData(it) }
        }
    }
}
