package ru.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel

data class RecipeState(
    val title: String? = null,
    val imageUrl: String? = null,
    val ingredients: List<String> = emptyList(),
    val method: List<String> = emptyList(),
    val isFavorite: Boolean = false
)

class RecipeViewModel : ViewModel() {
    private var _state = RecipeState()
    val state: RecipeState
        get() = _state

    fun updateTitle(newTitle: String) {
        _state = _state.copy(title = newTitle)
    }

    fun updateImageUrl(newImageUrl: String) {
        _state = _state.copy(imageUrl = newImageUrl)
    }

    fun updateIngredients(newIngredients: List<String>) {
        _state = _state.copy(ingredients = newIngredients)
    }

    fun updateMethod(newMethod: List<String>) {
        _state = _state.copy(method = newMethod)
    }

    fun toggleFavorite() {
        _state = _state.copy(isFavorite = !_state.isFavorite)
    }
}