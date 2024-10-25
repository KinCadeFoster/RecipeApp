package ru.example.recipeapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.example.recipeapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorite: Boolean = false,
    val seekBarCount: Int = 1,
)



class RecipeViewModel : ViewModel() {
    private var _state = RecipeState()
    val state: RecipeState
        get() = _state
}