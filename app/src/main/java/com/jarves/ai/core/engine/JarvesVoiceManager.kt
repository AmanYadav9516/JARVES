package com.jarves.ai.core.engine

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer

class JarvesVoiceManager(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
    private val onVolumeChanged: (Float) -> Unit = {}
) {

    private var speechRecognizer: SpeechRecognizer? = null

    init {
        runCatching {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                    setRecognitionListener(createListener())
                }
            }
        }.onFailure {
            onError("Speech recognition initialisation fallback: ${it.localizedMessage}")
        }
    }

    fun startListening() {
        runCatching {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "hi-IN")
                putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, false)
            }
            speechRecognizer?.startListening(intent)
        }.onFailure {
            onError("Listening start error: ${it.localizedMessage}")
        }
    }

    fun stopListening() {
        runCatching {
            speechRecognizer?.stopListening()
        }
    }

    private fun createListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {}
        override fun onBeginningOfSpeech() {}
        override fun onRmsChanged(rmsdB: Float) {
            runCatching { onVolumeChanged(rmsdB) }
        }
        override fun onBufferReceived(buffer: ByteArray?) {}
        override fun onEndOfSpeech() {}
        override fun onError(error: Int) {
            runCatching { onError("Speech Error code: $error") }
        }
        override fun onResults(results: Bundle?) {
            runCatching {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                val speechText = matches?.firstOrNull() ?: ""
                if (speechText.isNotEmpty()) {
                    onResult(speechText)
                }
            }
        }
        override fun onPartialResults(partialResults: Bundle?) {}
        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    fun destroy() {
        runCatching {
            speechRecognizer?.destroy()
        }
    }
}
