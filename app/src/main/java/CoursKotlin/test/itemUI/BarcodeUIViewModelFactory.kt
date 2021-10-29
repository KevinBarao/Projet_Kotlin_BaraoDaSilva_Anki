package CoursKotlin.test.itemUI

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import CoursKotlin.test.itemUI.barcodeList.ItemListViewModel
import CoursKotlin.test.itemUI.barcodeDetail.BarcodeDetailViewModel
import CoursKotlin.test.repository.Repository
import java.lang.IllegalArgumentException

class BarcodeUIViewModelFactory(private val repository: Repository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ItemListViewModel::class.java) -> ItemListViewModel(repository)
            modelClass.isAssignableFrom(BarcodeDetailViewModel::class.java) -> BarcodeDetailViewModel(repository)
            else -> throw IllegalArgumentException("Unexpected model class $modelClass")
        } as T
    }

}