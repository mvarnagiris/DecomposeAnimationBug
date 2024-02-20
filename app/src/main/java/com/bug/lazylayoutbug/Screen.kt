package com.bug.lazylayoutbug

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.replaceAll
import kotlinx.coroutines.delay

var value = 0
var initialScreenLoadingDelay = 250L
var initialContentLoadingDelay = 10L

class Screen(
    private val navigation: StackNavigation<Route>,
    componentContext: ComponentContext,
) : ComponentContext by componentContext {

    @Composable
    fun Content() {
        ScreenContent(navigation)
    }

}

@Composable
private fun ScreenContent(navigation: StackNavigation<Route>) {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        var screenLoadingDelay by remember { mutableLongStateOf(initialScreenLoadingDelay) }
        var contentLoadingDelay by remember { mutableLongStateOf(initialContentLoadingDelay) }

        var isScreenLoading by remember { mutableStateOf(true) }
        var content by remember { mutableStateOf("LOADING!!!") }

        LaunchedEffect(isScreenLoading) {
            if (isScreenLoading) {
                // Mimic screen loading
                delay(screenLoadingDelay)
                value++
                isScreenLoading = false
            } else {
                // Mimic content loading after screen loads
                delay(contentLoadingDelay)
                content = value.toString()
                Log.d("ASD", "Loaded value = $value")
            }
        }

        if (isScreenLoading) {
            Box(Modifier.padding(innerPadding).fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(Modifier.padding(innerPadding).padding(16.dp)) {
                DelayChanger(
                    title = "Screen loading",
                    delayMs = screenLoadingDelay,
                    onInc = {
                        screenLoadingDelay += 10
                        initialScreenLoadingDelay = screenLoadingDelay
                    },
                    onDec = {
                        screenLoadingDelay = (screenLoadingDelay - 10).coerceAtLeast(10)
                        initialScreenLoadingDelay = screenLoadingDelay
                    },
                )

                DelayChanger(
                    title = "Content loading",
                    delayMs = contentLoadingDelay,
                    onInc = {
                        contentLoadingDelay += 5
                        initialContentLoadingDelay = contentLoadingDelay
                    },
                    onDec = {
                        contentLoadingDelay = (contentLoadingDelay - 5).coerceAtLeast(5)
                        initialContentLoadingDelay = contentLoadingDelay
                    },
                )

                Button(
                    onClick = { navigation.replaceAll(Route(System.currentTimeMillis())) },
                    modifier = Modifier.fillMaxWidth(),
                    content = { Text("Restart") }
                )

                HorizontalDivider()

                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(content)
                }
            }
        }

    }

}

@Composable
fun DelayChanger(
    title: String,
    delayMs: Long,
    onInc: () -> Unit,
    onDec: () -> Unit,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onDec) { Text("-") }
        Text("$title: ${delayMs}ms")
        Button(onInc) { Text("+") }
    }
}
