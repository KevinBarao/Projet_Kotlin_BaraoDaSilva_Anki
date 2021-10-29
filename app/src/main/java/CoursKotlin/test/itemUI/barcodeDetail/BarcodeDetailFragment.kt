package CoursKotlin.test.itemUI.barcodeDetail

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_basic_scan_detail.*
import kotlinx.android.synthetic.main.item_barcode.scansTextView
import CoursKotlin.test.App
import CoursKotlin.test.R
import CoursKotlin.test.extensions.setTransitionNameCompat
import CoursKotlin.test.extensions.toFormattedString
import CoursKotlin.test.itemUI.BarcodeUIViewModelFactory
import CoursKotlin.test.models.DataMatrix
import CoursKotlin.test.models.Barcode
import CoursKotlin.test.models.QRCode

class BarcodeDetailFragment : Fragment() {

    private lateinit var viewModel: BarcodeDetailViewModel
    private var itemId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        sharedElementEnterTransition = transition
        sharedElementReturnTransition = transition
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_basic_scan_detail, container, false)

        // GETTING ITEMID FROM ARGUMENT
        itemId = BarcodeDetailFragmentArgs.fromBundle(requireArguments()).itemId

        // BINDING VIEW
        view.findViewById<ImageView>(R.id.iconImageView).setTransitionNameCompat("icon", itemId)
        view.findViewById<TextView>(R.id.scansTextView).setTransitionNameCompat("code", itemId)
        view.findViewById<TextView>(R.id.efficiencyTextView).setTransitionNameCompat("date", itemId)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SUBSCRIBING TO DATA
        val factory = BarcodeUIViewModelFactory(App.repository)
        val viewModel: BarcodeDetailViewModel by viewModels { factory }
        viewModel.getBarcodeDetail(itemId).observe(viewLifecycleOwner, Observer { updateUi(it!!) })

        // FAB BUTTON
        fab.setOnClickListener { navigateToHome() }
    }

    // UPDATING UI FOR BOTH BARCODE
    private fun updateUi(barcode: Barcode) {
        when(barcode) {
            is QRCode -> {
                scansTextView.text = barcode.code
                efficiencyTextView.text = barcode.dateCreated.toFormattedString()
            }
            is DataMatrix -> {
                iconImageView.setImageResource(R.drawable.ic_data_matrix)
                noteTextView.text = "Data matrix"
                scansTextView.text = barcode.code
                efficiencyTextView.text = barcode.dateCreated.toFormattedString()
            }
        }
    }

    // NAVIGATE BACK BY THE FAB BUTTON
    private fun navigateToHome() {
        val previousDestination = findNavController().previousBackStackEntry?.destination
        if(previousDestination?.label == "QRCode / Data Matrix scanner") {
            findNavController().popBackStack()
            findNavController().popBackStack()
        } else {
            findNavController().popBackStack()
        }
    }

}