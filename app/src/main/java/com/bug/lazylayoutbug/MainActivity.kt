package com.bug.lazylayoutbug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.bug.lazylayoutbug.ui.theme.LazyLayoutBugTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootComponentContext = defaultComponentContext()
        val navigationHost = NavigationHost(rootComponentContext)

        setContent {
            LazyLayoutBugTheme {
                navigationHost.AppNavigation()
            }
        }
    }
}
