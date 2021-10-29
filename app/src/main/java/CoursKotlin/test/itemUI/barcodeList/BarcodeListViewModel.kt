package CoursKotlin.test.itemUI.barcodeList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import CoursKotlin.test.models.Barcode
import CoursKotlin.test.repository.Repository

data class ItemListViewState(
    val hasItemsChanged: Boolean,
    val isFabMenuOpen: Boolean,
    val barcodes: List<Barcode>
)

class ItemListViewModel(private val repository: Repository) : ViewModel() {

    private val viewState = MediatorLiveData<ItemListViewState>()

    // SETTING ITEMS
    init {
        viewState.addSource(repository.getAllItems()) { items ->
            val oldState = viewState.value!!
            viewState.value = oldState.copy(
                hasItemsChanged = true,
                barcodes = items
            )
        }
        //INITIAL STATE
        viewState.postValue(ItemListViewState(
            hasItemsChanged = false,
            isFabMenuOpen = false,
            barcodes = emptyList()
        ))
    }

    // GETTING VIEW STATE
    fun getViewState(): LiveData<ItemListViewState> = viewState

    // ADDING A BARCODE TO REPOSITORY
    fun addItem(barcode: Barcode) = repository.addItem(barcode)


    // FAB MENU TOGGLE
    fun toggleFabMenu() {
        val oldState = viewState.value!!
        viewState.value = oldState.copy(
            isFabMenuOpen = !oldState.isFabMenuOpen,
            hasItemsChanged = false
        )
    }

    // GETTING LAST ITEM ID
    fun getLastItemId(): Int? = repository.getLastItemId()

}