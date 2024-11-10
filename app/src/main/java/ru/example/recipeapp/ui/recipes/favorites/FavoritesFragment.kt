package ru.example.recipeapp.ui.recipes.favorites

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.example.recipeapp.R
import ru.example.recipeapp.data.STUB
import ru.example.recipeapp.databinding.FragmentFavoritesBinding
import ru.example.recipeapp.model.Recipe
import ru.example.recipeapp.ui.Constants
import ru.example.recipeapp.ui.Constants.KEY_FAVORITE_RECIPES
import ru.example.recipeapp.ui.Constants.SHARED_PREFS_NAME
import ru.example.recipeapp.ui.recipes.recipe.RecipeFragment
import ru.example.recipeapp.ui.recipes.recipe_list.RecipeAdapter

class FavoritesFragment : Fragment() {
    private val binding by lazy {
        FragmentFavoritesBinding.inflate(layoutInflater)
    }
    private val viewModel: FavoritesViewModel by viewModels()

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

        val favoriteIds = getFavorites()
        viewModel.loadFavorites(favoriteIds)

        viewModel.favorites.observe(viewLifecycleOwner) { favoritesRecipe ->
            updateRecyclerView(favoritesRecipe)
        }
    }

    private fun updateRecyclerView(favoritesRecipe: List<Recipe>) {
        if (favoritesRecipe.isEmpty()) {
            binding.tvEmptyList.visibility = View.VISIBLE
            binding.rvFavorites.visibility = View.GONE
        } else {
            binding.tvEmptyList.visibility = View.GONE
            binding.rvFavorites.visibility = View.VISIBLE
            val recipeAdapter = RecipeAdapter(favoritesRecipe)
            binding.rvFavorites.adapter = recipeAdapter

            recipeAdapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
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
