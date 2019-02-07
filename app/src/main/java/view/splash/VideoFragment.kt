package view.splash


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.android.wordspeak.R

import com.universalvideoview.UniversalVideoView
import kotlinx.android.synthetic.main.fragment_video.*
import util.Constants
import java.lang.Exception
import java.util.*


class VideoFragment : Fragment() {

    private var candidateTextScore = -1
    private var candidateWord: String? = null
    private var candidateTextPosition = -1
    private var position = -1

    private var isFullscreen: Boolean? = null
    private var cachedHeight: Int = -1

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_video, container, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // View events here
        position = arguments!!.get(VIDEO_POSITION) as Int

        buttonListenAgain.setOnClickListener{ startVoiceListener() }
    }

    override fun onStart() {
        super.onStart()
        playVideo(position)
    }

    private fun playVideo(position: Int) {

        // get the des
        val rawUrl = Constants.videoMappedList()[position].url
        val uri = Uri.parse("android.resource://${activity!!.packageName}/" + rawUrl)

        // Media Controller

        videoView.setVideoURI(uri)
        videoView.setMediaController(mediaController)

        videoView.setVideoViewCallback(object: UniversalVideoView.VideoViewCallback{
            override fun onBufferingStart(mediaPlayer: MediaPlayer?) {

            }

            override fun onBufferingEnd(mediaPlayer: MediaPlayer?) {

            }

            override fun onPause(mediaPlayer: MediaPlayer?) {
                showVoiceListener()
            }

            override fun onScaleChange(isFullscreen: Boolean) {
                this@VideoFragment.isFullscreen = isFullscreen
                if (isFullscreen) {
                    val layoutParams = videoView.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    videoView.layoutParams = layoutParams
                    //GONE the unconcerned views to leave room for video and controller
                    videoView.visibility = View.VISIBLE
                } else {
                    val layoutParams = videoView.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = this@VideoFragment.cachedHeight
                    videoView.layoutParams = layoutParams
                    videoView.visibility = View.VISIBLE
                }
            }

            override fun onStart(mediaPlayer: MediaPlayer?) {
                hideVoiceListener()
            }
        })
        videoView.start()
    }

    private fun startVoiceListener() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "I'm listening, what do you want to learn?")
        }

        try {
            startActivityForResult(intent, VideoFragment.REQUEST_CODE_SPEECH_INPUT)
        } catch (e: Exception) {
            // Catch Error
            Toast.makeText(activity, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode ==  VideoFragment.REQUEST_CODE_SPEECH_INPUT && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            handleTextFromSpeech(result!![0])
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
            playVideo(candidateTextPosition)

        } else {
            Toast.makeText(activity, getString(R.string.sentence_video_cannot_be_found), Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("RestrictedApi")
    private fun showVoiceListener() {
        buttonListenAgain.visibility = View.VISIBLE
        textViewListenerLabel.visibility = View.VISIBLE
    }

    @SuppressLint("RestrictedApi")
    private fun hideVoiceListener() {
        buttonListenAgain.visibility = View.GONE
        textViewListenerLabel.visibility = View.GONE
    }

    companion object {
        const val TAG = "VIDEO_FRAGMENT"
        const val REQUEST_CODE_SPEECH_INPUT = 100

        private const val VIDEO_POSITION = "VIDEO_POSITION"
        fun forVideo(position: Int) : VideoFragment {
            val args = Bundle()
            args.putInt(VIDEO_POSITION, position)

            val fragment = VideoFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
