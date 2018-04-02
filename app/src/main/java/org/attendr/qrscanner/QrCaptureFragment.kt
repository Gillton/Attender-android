package org.attendr.qrscanner

import android.Manifest
import android.app.Activity
import android.view.View
import android.app.Fragment
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.os.Looper
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import com.google.android.gms.vision.barcode.Barcode
import kotlinx.android.synthetic.main.fragment_qrcode_capture.*
import org.attendr.R
import org.attendr.utils.network.Promise


/**
 * @author paul@quarkworks.co (Paul Gillis)
 */
class QrCaptureFragment : Fragment() {

    private var promise: Promise<Barcode>? = null

    companion object {

        private const val TAG = "Barcode-reader"

        private const val CAMERA_PERMISSION = 2

        fun newFragment(): QrCaptureFragment {
            return QrCaptureFragment()
        }
    }

    /**
     * Initializes the UI and creates the detector pipeline.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val rootView = inflater.inflate(R.layout.fragment_qrcode_capture, container, false)
        checkForPermission(activity)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        promise = Promise()
        promise?.then {
            val url = it.displayValue
            if (url.contains("/h")) {
                onDetectedQrCode(url)
            }
        }
        preview?.promise = promise
    }

    private fun onDetectedQrCode(url: String) {
            /*
	         * Once it finds a valid URL it checks if that url is on the valid domain. If it is post the userId to
	         * the valid attendr URL and checks for the response to notify the user.
	         */
        val mainHandler = Handler(Looper.getMainLooper())
        //TODO
    }

    override fun onResume() {
        super.onResume()
        preview?.create()
    }

    override fun onPause() {
        super.onPause()
        preview?.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        preview?.release()
    }

    private fun checkForPermission(activity: Activity) {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION)
            }
            checkForPermission(activity)
        } else {
            preview?.create()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (grantResults.isNotEmpty() && requestCode != CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Camera permission granted - initialize the camera source")
            preview?.create()
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

}