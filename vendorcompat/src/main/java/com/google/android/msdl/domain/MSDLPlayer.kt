/*
 * Standalone Gradle build stub, see MSDLToken.kt for context.
 * No-op implementation: playToken() does nothing (msdlFeedback flag defaults to false anyway).
 */
package com.google.android.msdl.domain

import android.os.Vibrator
import com.google.android.msdl.data.model.MSDLToken
import com.google.android.msdl.logging.MSDLEvent
import java.util.concurrent.Executor

interface MSDLPlayer {
    fun playToken(token: MSDLToken, properties: InteractionProperties?)
    fun getHistory(): List<MSDLEvent>

    companion object {
        fun createPlayer(vibrator: Vibrator?, executor: Executor, hapticFeedbackConfig: Any?): MSDLPlayer =
            NoOpMSDLPlayer()
    }
}

private class NoOpMSDLPlayer : MSDLPlayer {
    private val history = mutableListOf<MSDLEvent>()

    override fun playToken(token: MSDLToken, properties: InteractionProperties?) {
        history.add(MSDLEvent(token))
    }

    override fun getHistory(): List<MSDLEvent> = history
}
