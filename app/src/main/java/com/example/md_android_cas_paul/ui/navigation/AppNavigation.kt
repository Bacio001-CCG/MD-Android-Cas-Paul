package com.example.md_android_cas_paul.ui.navigation

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

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = BookRepository(context)
    NavHost(navController = navController, startDestination = "home") {

        composable("home") {
            HomeScreen(onNavigateToSearch = { navController.navigate("search") })
        }

        composable("search") {
            val viewModel: SearchBookViewModel = viewModel(factory = BookViewModelFactory(repository))
            SearchScreen(viewModel = viewModel, onBookSelected = { book ->
                navController.navigate("book_detail/${book.title}/${Uri.encode(book.workKey)}")
            })
        }

        composable("book_detail/{bookTitle}/{workKey}") { backStackEntry ->
            val bookTitle = backStackEntry.arguments?.getString("bookTitle") ?: ""
            val workKey = backStackEntry.arguments?.getString("workKey")?.let { Uri.decode(it) } ?: ""
            BookDetailScreen(bookTitle = bookTitle, bookKey = workKey)
        }
    }
}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchBookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SearchBookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}