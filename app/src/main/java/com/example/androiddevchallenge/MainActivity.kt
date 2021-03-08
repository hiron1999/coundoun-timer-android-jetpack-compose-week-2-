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

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme
import com.example.androiddevchallenge.ui.theme.setTime

lateinit var timer: CountDownTimer
lateinit var context: Context

var progress = 1f
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = applicationContext
        setContent {
            MyTheme(darkTheme = isSystemInDarkTheme()) {

                UpdateClock()
                popupdisplay()
            }
        }
    }
}

fun milsec_to_stringtime(milsec: Long): String {
    val hr = milsec / (60 * 60 * 1000)
    val min = milsec % (60 * 60 * 1000) / 60000
    val sec = milsec % (60 * 60 * 1000) % 60000 / 1000
    var Timestring = "" + if (hr <10) "0" + hr.toString() else hr.toString()
    Timestring += ":" + if (min <10) "0" + min.toString() else min.toString()
    Timestring += ":" + if (sec <10) "0" + sec.toString() else sec.toString()
    return Timestring
}

// @Composable
// fun timerrunning(clockview: Clockview= viewModel()) {
//    val remaning : Long by clockview.remaning.observeAsState(clockview.milsec.value!!)
//
// }
@Composable
fun UpdateClock(clockview: Clockview = viewModel()) {

    val milsec: Long by clockview.milsec.observeAsState(initial = 0L)
    val total: Long by clockview.totaltime.observeAsState(initial = 0L)
    var running by rememberSaveable { mutableStateOf(false) }
    var progress: Float = if (total != 0L) milsec.toFloat() / total.toFloat() else 1f

    if (milsec <= 100L && milsec> 0L) {
        timer.cancel()
        progress = 0f
        running = false
    }
    display(
        milsec, progress, clockview, running,
        onstart = {

            if (running == true) {

                timer.cancel()
                running = false
            } else {
                if (milsec> 100L) {
                    running = true

                    setTimer(clockview.milsec.value!!, clockview = clockview)
                    timer.start()
                } else {
                    Toast.makeText(context, "Set a time grater than 0s", Toast.LENGTH_SHORT).show()
                    clockview.reset_time(0L)
                }
            }
        },
        onreset = {
            clockview.reset_time(total)
        }
    )
}

@Composable
fun popupdisplay(clockview: Clockview = viewModel()) {
    val isopen: Boolean by clockview.isopen.observeAsState(initial = false)
    setTime(call = isopen, onDismiss = { clockview.dismisspopup() }, clockview = clockview)
}

@Composable
fun display(
    time: Long,
    progress: Float,
    clockview: Clockview,
    running: Boolean,
    onstart: () -> Unit,
    onreset: () -> Unit,
) {

    ConstraintLayout(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {

        val (progressbar, text, start_stop, reset, timesup) = createRefs()

        CircularProgressIndicator(
            progress = progress,
            modifier = Modifier
                .padding(12.dp)
                .size(300.dp)

                .constrainAs(progressbar) {
                    top.linkTo(parent.top, margin = 60.dp)
//               bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },

        )
        Text(
            text = milsec_to_stringtime(time),

            modifier = Modifier
                .constrainAs(text) {
                    top.linkTo(progressbar.top)
                    bottom.linkTo(progressbar.bottom)
                    start.linkTo(progressbar.start)
                    end.linkTo(progressbar.end)
                }
            .clickable(
                    onClick = {
                       if(!running) clockview.showpopup() // popup for set time
                    }
                ),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h2

        )
        if (time <100L && time> 0L) {
            Text(
                text = "Time Up",

                modifier = Modifier
                    .constrainAs(timesup) {
                        top.linkTo(text.top)
                        bottom.linkTo(progressbar.bottom)
                        start.linkTo(progressbar.start)
                        end.linkTo(progressbar.end)
                    }
                    .clickable(
                        onClick = {
                            clockview.showpopup() // popup for set time
                        }
                    ),
                color = Color.Red,
                style = MaterialTheme.typography.caption

            )
        }

        Button(
            onClick = onstart,
            modifier = Modifier
                .constrainAs(start_stop) {
//                        top.linkTo(progressbar.bottom, margin = 100.dp)
//                        start.linkTo(reset.start, margin = 160.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .requiredWidth(175.dp)
                .padding(end = 12.dp, bottom = 20.dp),
            shape = RoundedCornerShape(15.dp)
        ) {
            Text(text = if (running) "Stop" else "Start", style = MaterialTheme.typography.button)
        }
        if (!running) {
            Button(
                onClick = onreset,
                modifier = Modifier
                    .constrainAs(reset) {
//                            top.linkTo(progressbar.bottom, margin = 100.dp)
                        start.linkTo(parent.start)
//                            end.linkTo(start_stop.end)
                        bottom.linkTo(parent.bottom)
                    }
                    .requiredWidth(175.dp)
                    .padding(start = 12.dp, bottom = 20.dp),
                shape = RoundedCornerShape(15.dp),

            ) {
                Text(text = "Reset", style = MaterialTheme.typography.button)
            }
        }
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
//       display()
    }
}

fun setTimer(milsec: Long, clockview: Clockview) { // timer built.........................................

    timer = object : CountDownTimer(milsec, 100) {
        override fun onTick(p0: Long) {
//            Log.d("timer", milsec_to_stringtime(p0))

            clockview.reset_time(p0)
        }

        override fun onFinish() {
        }
    }
}

// @Preview("Dark Theme", widthDp = 360, heightDp = 640)
// @Composable
// fun DarkPreview() {
//    MyTheme(darkTheme = true) {
//        MyApp()
//    }
// }
