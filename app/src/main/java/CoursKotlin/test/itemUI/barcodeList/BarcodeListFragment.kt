package CoursKotlin.test.itemUI.barcodeList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_barcode_list.*
import CoursKotlin.test.*
import CoursKotlin.test.extensions.FabSmall
import CoursKotlin.test.itemUI.BarcodeUIViewModelFactory
import CoursKotlin.test.models.DataMatrix
import CoursKotlin.test.models.Barcode
import CoursKotlin.test.models.QRCode

class BarcodeListFragment : Fragment(), BarcodeListAdapter.ItemListAdapterListener {

    private lateinit var viewModel: ItemListViewModel
    private lateinit var barcodeListAdapter: BarcodeListAdapter
    private val items = mutableListOf<Barcode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_barcode_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // SUBSCRIBING TO DATA
        val factory = BarcodeUIViewModelFactory(App.repository)
        val viewModel: ItemListViewModel by viewModels { factory } //With factory
        viewModel.getViewState().observe(viewLifecycleOwner, Observer { updateUi(it!!) })

        // SETTING ADAPTER
        barcodeListAdapter = BarcodeListAdapter(items, this)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = barcodeListAdapter

        // FAB BUTTONS
        fab.setOnClickListener { viewModel.toggleFabMenu() }
        fabAnalysisScan.setOnClickListener { navigateToAnalyseScan() }
        fabBasicScan.setOnClickListener { navigateToBasicScan() }
    }

    // ANIMATION WHEN FAB MENU IS CLOSED
    private fun openFABMenu() {
        ViewCompat.animate(fab)
            .rotation(45f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator(10f))
            .start()

        animateShowFab(fabAnalysisScan)
        animateShowFab(fabBasicScan)
    }

    // ANIMATION WHEN FAB MENU IS OPEN
    private fun closeFABMenu() {
        ViewCompat.animate(fab)
            .rotation(0f)
            .setDuration(300)
            .setInterpolator(OvershootInterpolator(10f))
            .start()

        animateHideFab(fabAnalysisScan)
        animateHideFab(fabBasicScan)
    }

    // ANIMATION TO SHOW SMALL FAB
    private fun animateShowFab(fab: FabSmall) {
        ViewCompat.animate(fab)
            .translationY(-fab.offsetYAnimation)
            .withStartAction { fab.visibility = View.VISIBLE }
            .withEndAction {
                fab.labelView.animate()
                    .alpha(1.0f)
                    .duration = 200
            }
    }

    // ANIMATION TO HIDE SMALL FAB
    private fun animateHideFab(fab: FabSmall) {
        ViewCompat.animate(fab)
            .translationY(0f)
            .withStartAction { fab.labelView.animate().alpha(0f)}
            .withEndAction { fab.visibility = View.GONE}
    }

    // NAVIGATION TO BASIC SCAN
    private fun navigateToBasicScan() {
        val action = BarcodeListFragmentDirections.actionItemListFragmentToBasicScanFragment()
        findNavController().navigate(action)
    }

    // NAVIGATION TO ANALYSIS SCAN
    private fun navigateToAnalyseScan() {
        val action = BarcodeListFragmentDirections.actionItemListFragmentToAnalysisScanFragment()
        findNavController().navigate(action)
    }

    // UPDATING UI
    private fun updateUi(state: ItemListViewState) {
        if (state.hasItemsChanged) {
            items.clear()
            items.addAll(state.barcodes)
            barcodeListAdapter.notifyDataSetChanged()
        }

        if(state.isFabMenuOpen) {
            openFABMenu()
        } else {
            closeFABMenu()
        }
    }

    // ON ITEM CLICK
    override fun onItemClicked(barcode: Barcode, itemView: View) {
        // BINDING ITEMS VIEW
        val iconView = itemView.findViewById<ImageView>(R.id.iconImageView)
        val codeTextView = itemView.findViewById<TextView>(R.id.scansTextView)
        val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)

        // TRANSITION FOR BARCODE ITEMS ON CLICK
        when(barcode) {
            is QRCode -> {
                val extras = FragmentNavigatorExtras(
                    iconView to iconView.getTransitionName(),
                    codeTextView to codeTextView.getTransitionName(),
                    dateTextView to dateTextView.getTransitionName()
                )

                val action =  BarcodeListFragmentDirections.actionItemListFragmentToItemBarcodeDetailFragment(barcode.id)
                findNavController().navigate(action, extras)
            }
            is DataMatrix -> {
                val extras = FragmentNavigatorExtras(
                    iconView to iconView.getTransitionName(),
                    codeTextView to codeTextView.getTransitionName(),
                    dateTextView to dateTextView.getTransitionName()
                )

                val action = BarcodeListFragmentDirections.actionItemListFragmentToItemBarcodeDetailFragment(barcode.id)
                findNavController().navigate(action, extras)
            }
        }
    }

}