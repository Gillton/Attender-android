package org.attendr.main

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.attendr.R
import org.attendr.classes.ClassDetailsActivity

/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val i = Intent(this, ClassDetailsActivity::class.java)
        startActivity(i)
    }
}