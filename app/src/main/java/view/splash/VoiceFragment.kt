package view.splash


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import com.android.wordspeak.R

import kotlinx.android.synthetic.main.fragment_voice.view.*
import timber.log.Timber
import util.Constants
import java.lang.Exception
import java.util.*



class VoiceFragment : Fragment() {

    companion object {
        const val TAG = "VOICE_FRAGMENT"
        const val REQUEST_CODE_SPEECH_INPUT = 100
    }

    private var candidateTextScore = -1
    private var candidateWord: String? = null
    private var candidateTextPosition = -1

    private lateinit var rootView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_voice, container, false)


        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rootView.buttonMic.setOnClickListener { startVoiceListener() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==  VoiceFragment.REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            handleTextFromSpeech(result!![0])
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Reset values
        candidateTextPosition = -1
        candidateTextScore = -1
        candidateWord = null
    }

    private fun startVoiceListener() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "I'm listening, what do you want to learn?")
        }

        try {
            startActivityForResult(intent, VoiceFragment.REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            // Catch Error
            Toast.makeText(activity, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleTextFromSpeech(text: String) {
        val keysToMatch = Constants.keyWordList()

        keysToMatch.forEachIndexed { index, list ->
            // Check through every word if it matches from the message from the user.
            list.forEach {
                val score = text.indexOf(it)

                if (score > candidateTextScore) {
                    candidateTextScore = score
                    candidateWord = it
                    candidateTextPosition = index
                }
            }
        }

        // If it matches, Play the video
        if (candidateTextScore != -1) {
            candidateTextScore = -1

            // Play the video
            redirectToVideoPlayer(candidateTextPosition)
        } else {
            Toast.makeText(activity, getString(R.string.sentence_video_cannot_be_found), Toast.LENGTH_LONG).show()
        }
    }

    private fun redirectToVideoPlayer(position: Int) {
        Timber.e("asdf redirectToVideoPlayer $position")
//        startActivity(Intent(activity, MainActivity::class.java))
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            (activity as SplashActivity).showVideoFragment(position)
        }
    }
}
