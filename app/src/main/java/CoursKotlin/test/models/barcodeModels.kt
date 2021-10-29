package CoursKotlin.test.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

sealed class Barcode {
    abstract var id: Int
    abstract var dateCreated: Date
}

@Parcelize
data class QRCode(override var id: Int = 0,
                  override var dateCreated: Date = Date(),
                  val code: String) : Barcode(), Parcelable
@Parcelize
data class DataMatrix(override var id: Int = 0,
                      override var dateCreated: Date = Date(),
                      val code: String) : Barcode(), Parcelable