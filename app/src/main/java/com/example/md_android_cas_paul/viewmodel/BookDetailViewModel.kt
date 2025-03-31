import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.md_android_cas_paul.data.repository.BookRepository

class BookDetailViewModel(
    private val repository: BookRepository,
    val workKey: String,
    val title: String,
    val author: String?
) : ViewModel() {

    var isFavorite = mutableStateOf(repository.isFavorite(workKey))
        private set

    fun toggleFavorite() {
        if (isFavorite.value) {
            repository.removeFavorite(workKey)
        } else {
            repository.addFavorite(workKey, title, author)
        }
        isFavorite.value = !isFavorite.value
    }
}