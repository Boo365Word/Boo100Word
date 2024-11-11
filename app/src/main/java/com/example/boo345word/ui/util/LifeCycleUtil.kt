package com.example.boo345word.ui.util

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch

/**
 *  flow 수집을 위한 함수를 조금 더 간결하게 표현하기 위해 만든 유틸성 함수
 */
fun repeatOnStarted(lifecycleOwner: LifecycleOwner, action: suspend () -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            action()
        }
    }
}
