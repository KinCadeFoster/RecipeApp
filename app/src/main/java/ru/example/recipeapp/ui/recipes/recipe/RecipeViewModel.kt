package ru.example.recipeapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.example.recipeapp.model.Recipe

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorite: Boolean = false,
    val portionCount: Int = 1,
)

class RecipeViewModel : ViewModel() {
    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    init {
        Log.d("RecipeViewModel", "Первичная инициализация RecipeViewModel")

        _recipeLiveData.value = RecipeState(isFavorite = true)
    }
}