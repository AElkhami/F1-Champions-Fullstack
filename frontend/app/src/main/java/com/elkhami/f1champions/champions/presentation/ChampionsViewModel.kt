package com.elkhami.f1champions.champions.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elkhami.f1champions.champions.domain.ChampionRepository
import com.elkhami.f1champions.core.network.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by A.Elkhami on 22/05/2025.
 */

@HiltViewModel
class ChampionsViewModel @Inject constructor(
    private val repository: ChampionRepository
) : ViewModel() {

    var uiState by mutableStateOf(ChampionsUiState())
        private set

    fun loadChampions() {
        // Prevent reloading if already loaded
        if (uiState.champions.isNotEmpty() || uiState.isLoading) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true)

            when(val result = repository.getChampions()){
                is Result.Error -> {
                    uiState = uiState.copy(
                        isLoading = false,
                        error = result.error
                    )
                }
                is Result.Success -> {
                    uiState = uiState.copy(
                        champions = result.data.map {
                            ChampionItemUiState(
                                season = it.season,
                                driver = it.driverName,
                                constructor = it.constructor,
                                title = ""
                            )
                        },
                        isLoading = false
                    )
                }
            }
        }
    }

    fun refreshChampions() {
        viewModelScope.launch {
            uiState = uiState.copy(isRefreshing = true)

            when (val result = repository.getChampions()) {
                is Result.Error -> {
                    uiState = uiState.copy(
                        isRefreshing = false,
                        error = result.error
                    )
                }

                is Result.Success -> {
                    uiState = uiState.copy(
                        champions = result.data.map {
                            ChampionItemUiState(
                                season = it.season,
                                driver = it.driverName,
                                constructor = it.constructor,
                                title = ""
                            )
                        },
                        isRefreshing = false,
                        error = null
                    )
                }
            }
        }
    }

}