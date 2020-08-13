package com.shadi.feature.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.shadi.data.response.Result
import com.shadi.repository.MatchMakingRepository
import javax.inject.Inject

/**
 * View Model class for MatchMaking Fragment
 */
class MatchMakingViewModel @ViewModelInject constructor(private val repository: MatchMakingRepository) :
    ViewModel() {
    val matchList = repository.getMatchList().cachedIn(viewModelScope)

    suspend fun updateMatch(result: Result){
      repository.updateMatch(result)
    }
}