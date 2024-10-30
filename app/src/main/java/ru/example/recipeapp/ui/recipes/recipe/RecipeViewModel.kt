package ru.example.recipeapp.ui.recipes.recipe

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.example.recipeapp.data.STUB.getRecipeById
import ru.example.recipeapp.model.Recipe
import ru.example.recipeapp.ui.Constants.KEY_FAVORITE_RECIPES
import ru.example.recipeapp.ui.Constants.SHARED_PREFS_NAME

data class RecipeState(
    val recipe: Recipe? = null,
    val isFavorite: Boolean = false,
    val portionCount: Int = 1,
    val recipeImage: Drawable? = null,
)

class RecipeViewModel(context: Context, recipeId: Int?) : ViewModel() {
    private val _recipeLiveData = MutableLiveData<RecipeState>()
    val recipeLiveData: LiveData<RecipeState> = _recipeLiveData

    private val sharedPrefs by lazy {
        context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
    }

    init {
        recipeId?.let { loadRecipe(it, context) }
    }

    private fun loadRecipe(recipeId: Int, context: Context) {
        // TODO: load from network
        val loadedRecipe = getRecipeById(recipeId)
        val isFavorite = recipeId.toString() in getFavorites()
        val recipeImage = loadedRecipe?.imageUrl?.let { imageUrl ->
            context.assets.open(imageUrl).use { inputStream ->
                BitmapDrawable(context.resources, BitmapFactory.decodeStream(inputStream))
            }
        }
        _recipeLiveData.value = RecipeState(
            recipe = loadedRecipe,
            isFavorite = isFavorite,
            recipeImage = recipeImage
        )
    }

    fun onFavoritesClicked() {
        val currentRecipeState = _recipeLiveData.value ?: return
        val recipeId = currentRecipeState.recipe?.id?.toString() ?: return
        val updatedFavorites = getFavorites()

        val newFavoriteStatus = if (currentRecipeState.isFavorite) {
            updatedFavorites.remove(recipeId)
            false
        } else {
            updatedFavorites.add(recipeId)
            true
        }

        saveFavorites(updatedFavorites)
        _recipeLiveData.value = currentRecipeState.copy(isFavorite = newFavoriteStatus)
    }

    private fun getFavorites(): MutableSet<String> {
        val savedFavorites = sharedPrefs.getStringSet(KEY_FAVORITE_RECIPES, emptySet())
        return HashSet(savedFavorites ?: emptySet())
    }

    private fun saveFavorites(favoriteIds: Set<String>) {
        sharedPrefs.edit().putStringSet(KEY_FAVORITE_RECIPES, favoriteIds).apply()
    }

    fun updatePortions(portions: Int) {
        _recipeLiveData.value?.let { currentRecipeState ->
            _recipeLiveData.value = currentRecipeState.copy(portionCount = portions)
        }
    }
}