package ru.example.recipeapp.ui.recipes.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.example.recipeapp.data.STUB
import ru.example.recipeapp.model.Recipe

class FavoritesViewModel : ViewModel() {

    private val _favorites = MutableLiveData<List<Recipe>>()
    val favorites: LiveData<List<Recipe>> get() = _favorites

    fun loadFavorites(favoriteIds: Set<Int>) {
        val favoriteRecipes = STUB.getRecipesByIds(favoriteIds)
        _favorites.value = favoriteRecipes
    }
}
