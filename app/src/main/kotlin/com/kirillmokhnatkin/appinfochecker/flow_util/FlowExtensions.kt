package com.kirillmokhnatkin.appinfochecker.flow_util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun <T> Flow<T>.collectValues(
    scope: CoroutineScope,
    lifecycle: Lifecycle,
    collector: suspend (T) -> Unit
) {
    flowWithLifecycle(lifecycle)
        .onEach(collector)
        .launchIn(scope)
}
