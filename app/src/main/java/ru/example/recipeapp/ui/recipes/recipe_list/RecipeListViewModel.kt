package ru.example.recipeapp.ui.recipes.recipe_list

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.example.recipeapp.data.STUB
import ru.example.recipeapp.model.Category
import ru.example.recipeapp.model.Recipe


data class RecipeCategoryState(
    val recipes: List<Recipe> = emptyList(),
    val category: Category? = null,
    val categoryImage: Drawable? = null,
)

class RecipesListViewModel : ViewModel() {
    private val _state = MutableLiveData<RecipeCategoryState>()
    val state: LiveData<RecipeCategoryState> get() = _state

    fun loadCategoryData(category: Category?, context: Context) {
        category?.let {
            val recipes = STUB.getRecipesByCategoryId(it.id)
            val categoryImage = it.imageUrl.let { imageUrl ->
                context.assets.open(imageUrl).use { inputStream ->
                    BitmapDrawable(context.resources, BitmapFactory.decodeStream(inputStream))
                }
            }
            _state.value = RecipeCategoryState(recipes = recipes, category = it, categoryImage = categoryImage)
        }
    }
}