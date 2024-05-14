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

package com.appcarestudio.fanserials.data

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.appcarestudio.fanserials.core.data.DefaultFanSerialRepository
import com.appcarestudio.fanserials.core.database.FanSerial
import com.appcarestudio.fanserials.core.database.FanSerialDao

/**
 * Unit tests for [DefaultFanSerialRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultFanSerialRepositoryTest {

    @Test
    fun fanSerials_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultFanSerialRepository(FakeFanSerialDao())

        repository.add("Repository")

        assertEquals(repository.fanSerials.first().size, 1)
    }

}

private class FakeFanSerialDao : FanSerialDao {

    private val data = mutableListOf<FanSerial>()

    override fun getFanSerials(): Flow<List<FanSerial>> = flow {
        emit(data)
    }

    override suspend fun insertFanSerial(item: FanSerial) {
        data.add(0, item)
    }
}
