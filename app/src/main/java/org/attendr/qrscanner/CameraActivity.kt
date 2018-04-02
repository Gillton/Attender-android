package org.attendr.qrscanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.attendr.R
import kotlinx.android.synthetic.main.activity_camera.*


/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        navView.setNavigationItemSelectedListener {
            it.isChecked = true
            drawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }
    }
}