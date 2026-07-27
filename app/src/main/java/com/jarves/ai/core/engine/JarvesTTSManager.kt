package com.jarves.ai.core.engine

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class JarvesTTSManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isReady = false

    init {
        runCatching {
            tts = TextToSpeech(context, this)
        }
    }

    override fun onInit(status: Int) {
        runCatching {
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale("hi", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts?.language = Locale.US
                }
                isReady = true
            }
        }
    }

    fun speak(text: String) {
        runCatching {
            if (isReady) {
                tts?.speak(text, TextToSpeech.QUEUE_ADD, null, "JARVES_TTS_${System.currentTimeMillis()}")
            }
        }
    }

    fun shutdown() {
        runCatching {
            tts?.stop()
            tts?.shutdown()
        }
    }
}
