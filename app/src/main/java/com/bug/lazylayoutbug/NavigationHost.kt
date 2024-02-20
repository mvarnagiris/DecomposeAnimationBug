package com.bug.lazylayoutbug

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimator
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimator
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack

class NavigationHost(componentContext: ComponentContext) : ComponentContext by componentContext {
    private val navigation = StackNavigation<Route>()
    private val stack = childStack(
        source = navigation,
        serializer = null,
        handleBackButton = true,
        initialStack = { listOf(Route(System.currentTimeMillis())) },
        childFactory = { _, componentContext -> Screen(navigation, componentContext) },
    )

    @Composable
    fun AppNavigation() {
        Children(
            stack,
            Modifier.fillMaxSize(),
            animation = stackAnimation(
                disableInputDuringAnimation = true,
                selector = { _, _, _ -> noAnimation() },
            ),
            content = { child ->
                child.instance.Content()
            },
        )
    }
}

fun noAnimation(): StackAnimator = stackAnimator { _, _, content -> content(Modifier) }
