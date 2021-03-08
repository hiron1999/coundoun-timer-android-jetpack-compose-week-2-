/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Clockview : ViewModel() {
    private val _milsec = MutableLiveData(0L)
    private val _isopen = MutableLiveData(true)

    val milsec: LiveData<Long> = _milsec
    val isopen: LiveData<Boolean> = _isopen
    private val _Totaltime = MutableLiveData(milsec.value!!)
    val totaltime: LiveData<Long> = _Totaltime

    fun showpopup() { _isopen.value = false }
    fun dismisspopup() { _isopen.value = true }

    fun timerset(h: Long, m: Long, s: Long) {
        var t = ((h * 60 * 60) + (m * 60) + s) * 1000
        _milsec.value = t
        _isopen.value = true
        _Totaltime.value = t
    }
    fun reset_time(r: Long) {
        _milsec.value = r
    }
}
