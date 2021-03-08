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
package com.example.androiddevchallenge.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.androiddevchallenge.Clockview

var hh = 0L
var mm = 0L
var ss = 0L

@Composable
fun digits(digit: Long, time: Char): Long {
    var localdigit by remember { mutableStateOf(digit) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp)
    ) {
        IconButton(
            onClick = {
                localdigit = kotlin.math.min(localdigit + 1L, if (time.equals('H')) 23L else 59L)
                Log.e("up", localdigit.toString() + time)
            }
        ) {
            Icon(Icons.Filled.ExpandLess, contentDescription = "")
        }
        Text(
            text = if (digit <10L)"0" + localdigit.toString() else localdigit.toString(),
            style = MaterialTheme.typography.h4
        )
        IconButton(
            onClick = {
                localdigit = kotlin.math.max(localdigit - 1L, 0L)
                Log.e("down", localdigit.toString() + time)
            }
        ) {
            Icon(Icons.Filled.ExpandMore, contentDescription = "")
        }
    }
    return localdigit
}
@Composable
fun setTime(
    call: Boolean,
    onDismiss: () -> Unit,
    clockview: Clockview,

) {
    var flag = 1
    if (!call) {
        AlertDialog(

            onDismissRequest = onDismiss,

            title = { },
            text = {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Set Time",

                    )

                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically

                    ) {
                        hh = digits(hh, 'H')
                        Text(text = "Hours", style = MaterialTheme.typography.subtitle1)
                        mm = digits(mm, 'M')
                        Text(text = "Min", style = MaterialTheme.typography.subtitle1)
                        ss = digits(ss, 'S')
                        Text(text = "Sec", style = MaterialTheme.typography.subtitle1)
                    }
                }
            },

//            backgroundColor = MaterialTheme.colors.onBackground
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            confirmButton = {
                Button(

                    onClick = { clockview.timerset(hh, mm, ss) },

                ) {
                    Text(text = "Save", style = MaterialTheme.typography.button)
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss,

                ) {
                    Text(text = "Cancel", style = MaterialTheme.typography.button)
                }
            }
        )
    } else {
    }
}
@Preview()
@Composable
fun alartview() {
    MyTheme {
//            digits()
    }
}
