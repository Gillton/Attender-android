package org.attendr.qrscanner

import android.view.SurfaceHolder
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import com.google.android.gms.vision.CameraSource
import android.view.SurfaceView
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.attendr.utils.network.Promise


/**
 * @author paul@quarkworks.co (Paul Gillis)
 */

class CameraSourcePreview @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defAttrStyle: Int = 0) : SurfaceView(context, attrs, defAttrStyle) {
    private var surfaceAvailable = false
    private var isRunning = false
    private var cameraSource: CameraSource? = null

    var promise: Promise<Barcode>? = null

    companion object {
        private const val TAG = "CameraSourcePreview"
    }

    init {
        holder.addCallback(SurfaceCallback())
        requestLayout()
    }

    fun create() {
        val barcodeDetector = BarcodeDetector.Builder(context)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build()

        val tracker = object : Tracker<Barcode>() {
            override fun onNewItem(id: Int, barcode: Barcode?) {
                barcode?.displayValue?.let {
                    promise?.resolve(barcode)
                }
            }
        }

        val multiProcessorFactory = MultiProcessor.Factory<Barcode>  { tracker }

        val multiProcessor = MultiProcessor.Builder(multiProcessorFactory).build()

        barcodeDetector.setProcessor(multiProcessor)

        if (barcodeDetector.isOperational && width != 0 && height != 0) {
            cameraSource = CameraSource.Builder(context, barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(width, height)
                    .setRequestedFps(24.0f)
                    .setAutoFocusEnabled(true)
                    .build()
            start()
        } else {
            Log.w(TAG, "Error: Barcode Detector isn't operational")
        }
    }

    fun start() {
        cameraSource?.let {
            if (surfaceAvailable) {
                try {
                    cameraSource?.start(holder)
                    isRunning = true
                } catch (e: SecurityException) {
                    Log.e(TAG, "You do not have permission to start the camera", e)
                    release()
                }
            }
        }
    }

    fun stop() {
        isRunning = false
        cameraSource?.stop()
    }

    fun release() {
        if (isRunning) {
            stop()
        }
        cameraSource?.release()
        cameraSource = null
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val size = cameraSource?.previewSize
        val previewWidth = size?.width ?: right - left
        val previewHeight = size?.height ?: bottom - top

        setMeasuredDimension(previewWidth, previewHeight)

        create()

        start()
    }

    private inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surface: SurfaceHolder) {
            surfaceAvailable = true
            start()
        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            surfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    }
}