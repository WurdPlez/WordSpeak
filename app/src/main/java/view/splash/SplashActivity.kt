package view.splash

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Bundle
import com.android.wordspeak.R
import timber.log.Timber
import view.BaseActivity
import java.util.*
import kotlin.concurrent.schedule



class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timber.e("asdf Test")

        // Add splash fragment if this is first creation
        if (savedInstanceState == null) {
            val splashFragment = SplashFragment()

            // Load Splash Fragment
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentMainContainer, splashFragment, SplashFragment.TAG)
                .commit()
        }

        showVoiceFragment()
    }

    /** Shows the Voice listener Fragment */
    private fun showVoiceFragment() {
        val fragment = VoiceFragment()

        // Load Voice Fragment after some seconds.
        Timer("RedirectingToMainPage", false).schedule(3000) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentMainContainer, fragment, VoiceFragment.TAG)
                .commit()
        }
    }

    /** Shows the Video player Fragment */
    fun showVideoFragment(position: Int) {

        val fragment = VideoFragment.forVideo(position)
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("video")
            .replace(R.id.fragmentMainContainer, fragment, VideoFragment.TAG)
            .commit()
    }

    override fun onBackPressed() {

        val count = supportFragmentManager.backStackEntryCount

        if (count == 0) {
            super.onBackPressed()
            //additional code
        } else {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            supportFragmentManager.popBackStack()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}
