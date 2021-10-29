package CoursKotlin.test.scansUI

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_analysis_scan_detail.fab
import CoursKotlin.test.R

class AnalysisScanDetailFragment : Fragment() {

    private var scans: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_analysis_scan_detail, container, false)

        // GETTING SCAN NUMBER FROM ARGUMENTS
        scans = AnalysisScanDetailFragmentArgs.fromBundle(requireArguments()).scans

        // BINDING VIEW
        view.findViewById<TextView>(R.id.scansTextView).text = scans.toString()
        val note = view.findViewById<TextView>(R.id.noteTextView)
        val description = view.findViewById<TextView>(R.id.efficiencyTextView)

        // GIVING A NOTE DEPENDING THE SCAN NUMBER
        when(scans) {
             0,1,2,3 -> {
                note.text = "$scans / 10"
                description.text = "Le scanner n'est pas fiable"
             }
            4,5,6 -> {
                note.text = "$scans / 10"
                description.text = "Le scanner est peu fiable"
            }
            7,8,9 -> {
                note.text = "$scans / 10"
                description.text = "Le scanner est fiable"

            }
            else -> {
                note.text = "10 / 10"
                description.text = "Le scanner est très fiable"

            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // FAB BUTTON TO NAVIGATE TO HOME
        fab.setOnClickListener { navigateToHome() }
    }

    // NAVIGATE BACK TO HOME VIEW
    private fun navigateToHome(){
        findNavController().popBackStack()
        findNavController().popBackStack()
    }
}