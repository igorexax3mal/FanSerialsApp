/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.appcarestudio.fanserials.feature.fanserial.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.appcarestudio.fanserials.core.data.FanSerialRepository
import com.appcarestudio.fanserials.feature.fanserial.ui.FanSerialUiState.Error
import com.appcarestudio.fanserials.feature.fanserial.ui.FanSerialUiState.Loading
import com.appcarestudio.fanserials.feature.fanserial.ui.FanSerialUiState.Success
import javax.inject.Inject

@HiltViewModel
class FanSerialViewModel @Inject constructor(
    private val fanSerialRepository: FanSerialRepository
) : ViewModel() {

    val uiState: StateFlow<FanSerialUiState> = fanSerialRepository
        .fanSerials.map<List<String>, FanSerialUiState> { Success(data = it) }
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addFanSerial(name: String) {
        viewModelScope.launch {
            fanSerialRepository.add(name)
        }
    }
}

sealed interface FanSerialUiState {
    object Loading : FanSerialUiState
    data class Error(val throwable: Throwable) : FanSerialUiState
    data class Success(val data: List<String>) : FanSerialUiState
}
