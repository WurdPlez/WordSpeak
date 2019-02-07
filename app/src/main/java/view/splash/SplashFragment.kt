package view.splash


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle

import com.android.wordspeak.R
import timber.log.Timber

class SplashFragment : Fragment() {

    companion object {
        const val TAG = "SPLASH_FRAGMENT"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }
}
