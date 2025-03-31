package com.example.md_android_cas_paul.ui.navigation

import BookDetailViewModel
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.md_android_cas_paul.data.repository.BookRepository
import com.example.md_android_cas_paul.ui.detail.BookDetailScreen
import com.example.md_android_cas_paul.ui.home.HomeScreen
import com.example.md_android_cas_paul.ui.search.SearchScreen
import com.example.md_android_cas_paul.viewmodel.SearchBookViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.md_android_cas_paul.ui.favorite.FavoritesScreen
import com.example.md_android_cas_paul.ui.settings.SettingsScreen
import com.example.md_android_cas_paul.viewmodel.FavoritesViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = BookRepository(context)
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToFavorites = { navController.navigate("favorites") },
                onNavigateToSettings = { navController.navigate("settings") }
                )
        }

        composable("search") {
            val viewModel: SearchBookViewModel = viewModel(factory = SearchBookViewModelFactory(repository))
            SearchScreen(viewModel = viewModel, onBookSelected = { book ->
                navController.navigate("book_detail/${book.title}/${Uri.encode(book.workKey)}/${book.author}")
            })
        }
        composable("favorites") {
            val viewModel: FavoritesViewModel = viewModel(factory = SearchBookViewModelFactory(repository))
            FavoritesScreen(viewModel = viewModel, onBookSelected = { book ->
                navController.navigate("book_detail/${book.title}/${Uri.encode(book.workKey)}/${book.author}")
            })
        }
        composable("book_detail/{bookTitle}/{workKey}/{author}") { backStackEntry ->
            val bookTitle = backStackEntry.arguments?.getString("bookTitle") ?: ""
            val workKey = backStackEntry.arguments?.getString("workKey")?.let { Uri.decode(it) } ?: ""
            val author = backStackEntry.arguments?.getString("author") ?: ""
            val viewModel: BookDetailViewModel = viewModel(
                factory = SearchBookViewModelFactory(repository, workKey, bookTitle, author)
            )
            BookDetailScreen(viewModel = viewModel)
        }
        composable("settings") {
            SettingsScreen(onSave = { navController.popBackStack() })
        }
    }
}

class SearchBookViewModelFactory(
    private val repository: BookRepository,
    private val workKey: String? = null,
    private val title: String? = null,
    private val author: String? = null
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchBookViewModel::class.java) -> {
                SearchBookViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BookDetailViewModel::class.java) -> {
                if (workKey == null || title == null) {
                    throw IllegalArgumentException("BookDetailViewModel requires workKey and title")
                }
                BookDetailViewModel(repository, workKey, title, author) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}