package com.smartitventures.beardpapa.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import com.smartitventures.beardpapa.R

class ActivitySplash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler().postDelayed(Runnable {
            val mainIntent = Intent(this@ActivitySplash, ActivityLogin::class.java)
            startActivity(mainIntent)
            finish()
        }, 3000)
    }
}
