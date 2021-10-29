package CoursKotlin.test.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import CoursKotlin.test.models.Barcode
import java.util.*

class Repository {
    private val items = mutableListOf<Barcode>()
    private val itemsLiveData = MutableLiveData<List<Barcode>>()

    companion object {
        private var itemId = 1
    }

    // GETTING ALL BARCODES
    fun getAllItems(): LiveData<List<Barcode>> = itemsLiveData

    // GETTING LAST ITEM ID
    fun getLastItemId(): Int? = itemsLiveData.value?.first()?.id

    // ADDIND A BARCODE
    fun addItem(barcode : Barcode){
        barcode.id = itemId
        barcode.dateCreated = Date()
        items.add(0, barcode)
        itemId++

        itemsLiveData.postValue(items)
    }

    // FINDING A BARCODE BY ID
    @Suppress("UNCHECKED_CAST")
    fun <T : Barcode> findItemById(itemId: Int) : T? {
        return items.find { item -> item.id == itemId } as T?
    }
}