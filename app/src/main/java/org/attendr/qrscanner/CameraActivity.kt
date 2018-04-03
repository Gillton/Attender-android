package org.attendr.qrscanner

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.attendr.R


/**
 * @author Pauldg7@gmail.com (Paul Gillis)
 */
class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        fragmentManager.beginTransaction()
                .add(R.id.contentFrame, QrCaptureFragment.newFragment(), QrCaptureFragment.TAG)
                .addToBackStack(null)
                .commit()
    }
}