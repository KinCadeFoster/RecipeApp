package ru.example.recipeapp.ui.recipes.recipe_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.example.recipeapp.data.STUB
import ru.example.recipeapp.model.Recipe


class RecipesListViewModel : ViewModel() {

    private val _recipes = MutableLiveData<List<Recipe>>()
    val recipes: LiveData<List<Recipe>> get() = _recipes

    private val _categoryName = MutableLiveData<String?>()
    val categoryName: MutableLiveData<String?> get() = _categoryName

    private val _categoryImageUrl = MutableLiveData<String?>()
    val categoryImageUrl: MutableLiveData<String?> get() = _categoryImageUrl

    fun loadCategoryData(categoryId: Int?, categoryName: String?, categoryImageUrl: String?) {
        categoryId?.let {
            _recipes.value = STUB.getRecipesByCategoryId(it)
        }
        _categoryName.value = categoryName
        _categoryImageUrl.value = categoryImageUrl
    }
}
