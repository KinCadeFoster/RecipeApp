package ru.example.recipeapp.ui.recipes.recipe

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeViewModelFactory(
    private val context: Context,
    private val recipeId: Int?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecipeViewModel::class.java)) {
            return RecipeViewModel(context, recipeId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}