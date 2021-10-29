package CoursKotlin.test.scansUI

import android.Manifest
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.fragment_analysis_scan.*
import kotlinx.android.synthetic.main.fragment_analysis_scan.fab
import CoursKotlin.test.R

private const val CAMERA_REQUEST_CODE = 1

class AnalysisScanFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private var scanList: MutableList<String> = mutableListOf()
    private var timeLeftMilliseconds: Long = 16000
    private val timer = object: CountDownTimer(timeLeftMilliseconds, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            if(millisUntilFinished/1000 <= 1) {
                timerTextView.text = "0 secondes"
                codeScanner.stopPreview()
            }
            timerTextView.text = "${(millisUntilFinished/1000).toString()} secondes"
        }

        override fun onFinish() {
            navigateToDetailsView()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_analysis_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // LOCKING SCREEN ORIENTATION
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED;

        scanList.clear()
        codeScanner()

        // FAB BUTTON TO NAVIGATE TO HOME VIEW
        fab.setOnClickListener { navigateToHome() }
    }

    override fun onResume() {
        super.onResume()
        // ASKING FOR PERMISSION
        if (!hasCameraPermission()) {
            requestCameraPermission()
        } else {
            // STARTING TIMER AND SCANNER
            timer.start()
            codeScanner.startPreview()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // UNLOCKING SCREEN ORIENTATION, TURNING CAMERA OFF AND CANCEL THE TIMER
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        codeScanner.stopPreview()
        timer.cancel()
    }

    override fun onPause() {
        // FOR MEMORY LEAKS
        codeScanner.releaseResources()
        super.onPause()
    }

    // SETTING THE SCANNER
    private fun codeScanner() {
        codeScanner = CodeScanner(requireActivity(), analysisScanView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX)
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = false
            isFlashEnabled = false
            codeScanner.decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    scanResult(it)
                }
            }
        }
    }

    // GETTING THE SCAN RESULT BY THE BARCODE SCANNER
    private fun scanResult(scan: Result) {
        when(scan.barcodeFormat) {
            BarcodeFormat.QR_CODE -> {
                addingBarcodeToList(scan.text)
            }
            BarcodeFormat.DATA_MATRIX -> {
                addingBarcodeToList(scan.text)
            }
        }
    }

    // ADDING UNIQUE BARCODE TO A LIST
    private fun addingBarcodeToList(code: String) {
        if(!scanList.contains(code)) {
            scanList.add(code)
            countTextView.text = "${scanList.size.toString()} scan(s)"
        } else {
           Toast.makeText(context, "Code déjà scanné !", Toast.LENGTH_SHORT).show()
        }
    }

    // UNLOCKING SCREEN ORIENTATION, TURNING CAMERA AND TIMER OFF THEN NAVIGATE TO DETAIL VIEW
    private fun navigateToDetailsView() {
        codeScanner.stopPreview()
        timer.cancel()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR;
        val action = AnalysisScanFragmentDirections.actionAnalysisScanFragmentToAnalysisScanDetailFragment(scanList.size)
        findNavController().navigate(action)
    }

    // WATCHING IF APP HAS CAMERA PERMISSION
    private fun hasCameraPermission() = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    // REQUEST CAMERA PERMISSION
    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_REQUEST_CODE
        )
    }

    // ON REQUEST PERMISSION RESULT
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode) {
            CAMERA_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // START TO SCAN
                    codeScanner.startPreview()
                } else {
                    // NAVIGATE BACK
                    findNavController().popBackStack()
                }
        }
    }

    // NAVIGATE TO HOME VIEW
    private fun navigateToHome(){
        findNavController().popBackStack()
    }
}