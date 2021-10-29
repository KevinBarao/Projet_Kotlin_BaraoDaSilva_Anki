package CoursKotlin.test.itemUI.barcodeList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import CoursKotlin.test.models.DataMatrix
import CoursKotlin.test.models.Barcode
import CoursKotlin.test.models.QRCode
import CoursKotlin.test.R
import CoursKotlin.test.extensions.setTransitionNameCompat
import CoursKotlin.test.extensions.toFormattedString

class BarcodeListAdapter(
    private val barcodes: List<Barcode>,
    private val listener: ItemListAdapterListener? = null)
    : RecyclerView.Adapter<BarcodeListAdapter.ViewHolder>(), View.OnClickListener {

    interface ItemListAdapterListener {
        fun onItemClicked(barcode: Barcode, itemView: View)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // BINDING ITEMS VIEW
        val cardView = itemView.findViewById<CardView>(R.id.cardView)!!
        val iconView = itemView.findViewById<ImageView>(R.id.iconImageView)!!
        val dateTextView = itemView.findViewById<TextView>(R.id.dateTextView)!!
        val codeTextView = itemView.findViewById<TextView>(R.id.scansTextView)!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_barcode, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barcode = barcodes[position]
        with (holder) {
            cardView.tag = barcode
            cardView.setOnClickListener(this@BarcodeListAdapter)

            // SETTING IMAGE AND TEXT DEPENDING THE BARCODE
            when(barcode) {
                is QRCode -> {
                    codeTextView.text = barcode.code
                    iconView.setImageResource(R.drawable.ic_qrcode)
                }
                is DataMatrix -> {
                    codeTextView.text = barcode.code
                    iconView.setImageResource(R.drawable.ic_data_matrix)
                }
            }

            // FORMATING DATE
            dateTextView.text = barcode.dateCreated.toFormattedString()

            // ADDING SOME TRANSITIONS
            codeTextView.setTransitionNameCompat("code", barcode.id)
            dateTextView.setTransitionNameCompat("date", barcode.id)
            iconView.setTransitionNameCompat("icon", barcode.id)
        }
    }

    // GETTING ITEM COUNT
    override fun getItemCount(): Int  = barcodes.size

    // ONCLICK FUNCTION ON ITEM CLICK
    override fun onClick(v: View) {
        listener?.onItemClicked(v.tag as Barcode, v)
    }
}