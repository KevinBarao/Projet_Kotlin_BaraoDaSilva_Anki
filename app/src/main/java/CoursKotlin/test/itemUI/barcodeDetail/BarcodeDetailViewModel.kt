package CoursKotlin.test.itemUI.barcodeDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import CoursKotlin.test.models.Barcode
import CoursKotlin.test.repository.Repository

class BarcodeDetailViewModel(private val repository: Repository) : ViewModel() {
    private val barcodeLiveData = MutableLiveData<Barcode>()

    // GET THE ITEM DETAILS BY ITEMID
    fun getBarcodeDetail(itemId: Int) : LiveData<Barcode> {
        barcodeLiveData.value = repository.findItemById(itemId)!!
        return barcodeLiveData
    }
}