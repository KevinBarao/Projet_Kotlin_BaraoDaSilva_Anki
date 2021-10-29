package CoursKotlin.test.scansUI

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.Result
import kotlinx.android.synthetic.main.fragment_basic_scan.*
import CoursKotlin.test.App
import CoursKotlin.test.R
import CoursKotlin.test.itemUI.BarcodeUIViewModelFactory
import CoursKotlin.test.itemUI.barcodeList.ItemListViewModel
import CoursKotlin.test.models.*

private const val CAMERA_REQUEST_CODE = 1

class BasicScanFragment : Fragment() {

    private lateinit var codeScanner: CodeScanner
    private lateinit var viewModel: ItemListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basic_scan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FAB BUTTON TO NAVIGATE TO HOME VIEW
        fab.setOnClickListener { navigateToHome() }

        // SUBSCRIBING TO DATA
        val factory = BarcodeUIViewModelFactory(App.repository)
        viewModel = ViewModelProviders.of(requireActivity(), factory).get(ItemListViewModel::class.java)
        codeScanner()
    }

    override fun onResume() {
        super.onResume()
        // ASKING FOR PERMISSION
        if (!hasCameraPermission()) {
            requestCameraPermission()
        } else {
            // STARTING SCANNER
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        // FOR MEMORY LEAK
        codeScanner.releaseResources()
        super.onPause()
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

    // GETTING THE SCAN RESULT BY THE BARCODE SCANNER
    private fun scanResult(scan: Result) {
        when(scan.barcodeFormat) {
            BarcodeFormat.QR_CODE -> {
                viewModel.addItem(QRCode(code = scan.text))
                navigateToBarcodeDetail()
            }
            BarcodeFormat.DATA_MATRIX -> {
                viewModel.addItem(DataMatrix(code = scan.text))
                navigateToBarcodeDetail()
            }
        }

    }

    // NAVIGATE TO BARCODE DETAIL VIEW WITH ITEM ID IN ARGUMENTS
    private fun navigateToBarcodeDetail() {
        var itemId = viewModel.getLastItemId()
        if (itemId == null) itemId = 1
        val action = BasicScanFragmentDirections.actionBasicScanFragmentToItemBarcodeDetailFragment(itemId)
        findNavController().navigate(action)
    }

    // SETTING THE SCANNER
    private fun codeScanner() {
        codeScanner = CodeScanner(requireActivity(), basicScanView)

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.DATA_MATRIX)
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = false
            isFlashEnabled = false
            codeScanner.decodeCallback = DecodeCallback {
                requireActivity().runOnUiThread {
                    scanResult(it)
                }
            }
        }
    }

    // NAVIGATE TO HOME VIEW
    private fun navigateToHome() {
        findNavController().popBackStack()
    }
}