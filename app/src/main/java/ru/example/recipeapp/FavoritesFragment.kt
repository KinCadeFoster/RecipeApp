package ru.example.recipeapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.example.recipeapp.Constants.KEY_FAVORITE_RECIPES
import ru.example.recipeapp.Constants.SHARED_PREFS_NAME
import ru.example.recipeapp.databinding.FragmentFavoritesBinding
import ru.example.recipeapp.model.Recipe
import ru.example.recipeapp.model.STUB


class FavoritesFragment : Fragment() {
    private val binding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }

    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences(
            SHARED_PREFS_NAME,
            Context.MODE_PRIVATE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        val favoritesRecipe = loadFavorites()

        if (favoritesRecipe.isEmpty()){
            binding.tvEmptyList.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvEmptyList.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
            val recipeAdapter = RecipeAdapter(favoritesRecipe)
            binding.rvFavorites.adapter = recipeAdapter

        recipeAdapter.setOnItemClickListener(object :
            RecipeAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
        }
    }

    private fun loadFavorites(): List<Recipe> {
        val favoriteIds = getFavorites()
        val favoriteRecipes = STUB.getRecipesByIds(favoriteIds)
        return favoriteRecipes
    }

    private fun getFavorites(): Set<Int> {
        val savedFavorites = sharedPrefs.getStringSet(KEY_FAVORITE_RECIPES, emptySet())
        return savedFavorites?.map { it.toInt() }?.toSet() ?: emptySet()
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val recipe = STUB.getRecipeById(recipeId)

        if (recipe != null) {
            val bundle = Bundle().apply {
                putParcelable(Constants.ARG_RECIPE, recipe)
            }
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecipeFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.text_recipe_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}