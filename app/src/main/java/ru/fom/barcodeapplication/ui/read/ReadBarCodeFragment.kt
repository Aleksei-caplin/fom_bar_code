package ru.fom.barcodeapplication.ui.read

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.view.Surface
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.fragment_read_bar_code.*
import ru.fom.barcodeapplication.R
import ru.fom.barcodeapplication.RootActivity
import ru.fom.barcodeapplication.ui.base.BaseFragment
import ru.fom.barcodeapplication.viewmodels.ReadCodeViewModel
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ReadBarCodeFragment : BaseFragment<ReadCodeViewModel>() {
    override val viewModel: ReadCodeViewModel by viewModels()
    override val layout: Int = R.layout.fragment_read_bar_code

    private var imageCapture: ImageCapture? = null

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService



    /*private val framesAnalyzer: ImageAnalysis.Analyzer by lazy {
        ImageAnalysis.Analyzer(viewModel::onFrameReceived)
    }*/

    override fun setupViews() {

        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC,
                Barcode.FORMAT_CODABAR)
            .build()



        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                main, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        // Set up the listener for take photo button
        //camera_capture_button.setOnClickListener { takePhoto() }
        takePhoto()
        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        Log.d("M_photo", "fff")
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(main)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            /*val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewFinder.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }*/


            val preview = Preview.Builder()
                /*.setTargetRotation(Surface.ROTATION_0)
                .setTargetAspectRatio(screenAspectRatio)*/
                .build()
                .also { it.setSurfaceProvider(viewFinder.surfaceProvider) }

            val imageAnalyzer = ImageAnalysis.Builder()
                /*.setTargetRotation(Surface.ROTATION_0)
                .setTargetAspectRatio(screenAspectRatio)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)*/
                .build()
                .also { it.setAnalyzer(cameraExecutor, CustomAnalyzer(main) { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(main))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            main.baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getOutputDirectory(): File {
        val mediaDir = main.externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() } }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else main.filesDir
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(main,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                main.finish()
            }
        }
    }

    private class LuminosityAnalyzer(private val listener:(Any?) -> Unit) : ImageAnalysis.Analyzer {

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        override fun analyze(image: ImageProxy) {

            val buffer = image.planes[0].buffer
            val data = buffer.toByteArray()
            val pixels = data.map { it.toInt() and 0xFF }
            val luma = pixels.average()

            listener.invoke(luma)

            image.close()
        }
    }

    private class CustomAnalyzer(val context: RootActivity, private val listener:(Any?) -> Unit) : ImageAnalysis.Analyzer {

        val scanner = BarcodeScanning.getClient()

        private fun ByteBuffer.toByteArray(): ByteArray {
            rewind()    // Rewind the buffer to zero
            val data = ByteArray(remaining())
            get(data)   // Copy the buffer into a byte array
            return data // Return the byte array
        }

        @SuppressLint("UnsafeOptInUsageError")
        override fun analyze(imageProxy: ImageProxy
        ) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                val result = scanner.process(image).addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        when(barcode.valueType) {
                            Barcode.TYPE_WIFI -> {
                                val ssid = barcode.wifi!!.ssid
                                val password = barcode.wifi!!.password
                                val type = barcode.wifi!!.encryptionType
                                Toast.makeText(context, "TYPE_WIFI", Toast.LENGTH_LONG).show()
                            }
                            Barcode.TYPE_URL -> {
                                val title = barcode.url!!.title
                                val url = barcode.url!!.url
                                Toast.makeText(context, "TYPE_URL", Toast.LENGTH_LONG).show()
                            }
                            Barcode.FORMAT_QR_CODE -> {
                                Toast.makeText(context, barcode.displayValue, Toast.LENGTH_LONG).show()
                            }
                            Barcode.TYPE_TEXT -> {
                                Toast.makeText(context, barcode.displayValue, Toast.LENGTH_LONG).show()
                            }
                            Barcode.TYPE_UNKNOWN -> {
                            Toast.makeText(context, barcode.displayValue, Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            //listener.invoke(image)

            imageProxy.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

}