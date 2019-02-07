package view.main

import android.os.Bundle
import com.android.wordspeak.R
import view.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        actionBar.setTitle("Play Videoclip")
        actionBar.setDisplayHomeAsUpEnabled(true)
    }
}
