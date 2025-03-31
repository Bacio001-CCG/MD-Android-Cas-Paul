package com.example.md_android_cas_paul.ui.favorite
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.md_android_cas_paul.data.model.Book
import com.example.md_android_cas_paul.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(viewModel: FavoritesViewModel, onBookSelected: (Book) -> Unit) {
    LaunchedEffect(Unit) {
        viewModel.refreshFavorites()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        Text(
            text = "Favorite Books",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        if (viewModel.favorites.value.isEmpty()) {
            Text(
                text = "No favorite books yet!",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            LazyColumn {
                items(viewModel.favorites.value) { book ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onBookSelected(book) }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(text = book.title, fontSize = 18.sp)
                            book.author?.let { Text(text = "by $it", fontSize = 14.sp) }
                        }
                    }
                }
            }
        }
    }
}