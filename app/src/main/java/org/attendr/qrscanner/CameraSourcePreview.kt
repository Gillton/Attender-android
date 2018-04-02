package org.attendr.qrscanner

import android.Manifest
import android.view.SurfaceHolder
import android.content.Context
import android.support.annotation.RequiresPermission
import android.util.AttributeSet
import android.util.Log
import com.google.android.gms.vision.CameraSource
import android.view.SurfaceView
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.attendr.utils.network.Promise
import java.io.IOException


/**
 * @author paul@quarkworks.co (Paul Gillis)
 */

class CameraSourcePreview @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defAttrStyle: Int = 0) : SurfaceView(context, attrs, defAttrStyle) {
    private var mSurfaceAvailable: Boolean = false
    private var mCameraSource: CameraSource? = null
    var promise: Promise<Barcode>? = null

    companion object {
        private const val TAG = "CameraSourcePreview"
    }

    init {
        mSurfaceAvailable = false

        holder.addCallback(SurfaceCallback())
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
            mCameraSource = CameraSource.Builder(context, barcodeDetector)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(width, height)
                    .setRequestedFps(24.0f)
                    .setAutoFocusEnabled(true)
                    .build()
        } else {
            Log.w(TAG, "Error: Barcode Detector isn't operational")
        }
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    fun start(cameraSource: CameraSource?) {
        if (cameraSource == null) {
            stop()
        }

        mCameraSource = cameraSource
        mCameraSource?.let {
            try {
                startIfReady()
            } catch (e: SecurityException) {
                Log.e(TAG, "Unable to start camera source.", e)
                release()
            }
        }
    }

    fun stop() {
        mCameraSource?.stop()
    }

    fun release() {
        mCameraSource?.release()
        mCameraSource = null
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @Throws(IOException::class, SecurityException::class)
    private fun startIfReady() {
        if (mSurfaceAvailable) {
            mCameraSource?.start(holder)
        }
    }

    private inner class SurfaceCallback : SurfaceHolder.Callback {
        override fun surfaceCreated(surface: SurfaceHolder) {
            mSurfaceAvailable = true
            try {
                startIfReady()
            } catch (se: SecurityException) {
                Log.e(TAG, "Do not have permission to start the camera", se)
            } catch (e: IOException) {
                Log.e(TAG, "Could not start camera source.", e)
            }

        }

        override fun surfaceDestroyed(surface: SurfaceHolder) {
            mSurfaceAvailable = false
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val size = mCameraSource?.previewSize
        val previewWidth = size?.width ?: right - left
        val previewHeight = size?.height ?: bottom - top

        setMeasuredDimension(previewWidth, previewHeight)

        create()

        try {
            startIfReady()
        } catch (e: IOException) {
            Log.e(TAG, "Could not start camera source.", e)
        } catch (se: SecurityException) {
            Log.e(TAG, "Does not have permission to start the camera.", se)
        }

    }
}