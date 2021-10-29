package CoursKotlin.test.extensions

import android.view.View
import androidx.core.view.ViewCompat
import java.text.SimpleDateFormat
import java.util.*

private val dateFormatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
fun Date.toFormattedString() = dateFormatter.format(this)

fun View.getTransitionNameCompat() =
    ViewCompat.getTransitionName(this) ?: ""

fun View.setTransitionNameCompat(prefix: String, id: Any) =
    ViewCompat.setTransitionName(this, "$prefix$id")