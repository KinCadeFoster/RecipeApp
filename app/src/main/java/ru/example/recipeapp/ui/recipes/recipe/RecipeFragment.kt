package ru.example.recipeapp.ui.recipes.recipe

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.example.recipeapp.R
import ru.example.recipeapp.ui.Constants.KEY_FAVORITE_RECIPES
import ru.example.recipeapp.ui.Constants.SHARED_PREFS_NAME
import ru.example.recipeapp.databinding.FragmentRecipeBinding
import ru.example.recipeapp.model.Recipe
import ru.example.recipeapp.ui.Constants


class RecipeFragment : Fragment() {
    private val binding by lazy {
        FragmentRecipeBinding.inflate(layoutInflater)
    }
    private val ingredientsAdapter by lazy {
        IngredientsAdapter(emptyList())
    }

    private var isFavorite: Boolean = false

    private val sharedPrefs by lazy {
        requireContext().getSharedPreferences(SHARED_PREFS_NAME, android.content.Context.MODE_PRIVATE)
    }

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipeLiveData.observe(viewLifecycleOwner) { recipeLiveData ->
            Log.i("!!!", "isFavorite: ${recipeLiveData.isFavorite}")
        }

        val recipe = getRecipeFromArguments()

        if (recipe != null) {
            initUI(recipe)
            initRecycler(recipe)
            ingredientsAdapter.updateDataSet(recipe.ingredients)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.text_recipe_not_found),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getRecipeFromArguments(): Recipe? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(Constants.ARG_RECIPE)
        }
    }

    private fun initUI(recipe: Recipe) {
        binding.tVHeader.text = recipe.title

        val recipeUrl = recipe.imageUrl
        recipeUrl.let {
            val imageResources = requireContext().assets.open(it).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }

            binding.headerImage.setImageBitmap(imageResources)
            binding.headerImage.contentDescription = getString(
                R.string.content_description_recipe_image, recipe.title
            )
        }

        updateFavoriteIcon(getFavorites().contains(recipe.id.toString()))

        binding.imageHeartButton.setOnClickListener {
            toggleFavoriteState(recipe.id.toString())
        }
        binding.tVPortionNum.text = binding.seekBar.progress.toString()
    }

    private fun toggleFavoriteState(recipeId: String) {
        val favorites = getFavorites()

        isFavorite = if (favorites.contains(recipeId)) {
            favorites.remove(recipeId)
            false
        } else {
            favorites.add(recipeId)
            true
        }
        updateFavoriteIcon(isFavorite)
        saveFavorites(favorites)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconResId = if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.imageHeartButton.setImageResource(iconResId)
    }

    private fun initRecycler(recipe: Recipe) {
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMethod.layoutManager = LinearLayoutManager(requireContext())

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = MethodAdapter(recipe.method)

        val ingredientDivider = createDivider()
        val methodDivider = createDivider()

        binding.rvIngredients.addItemDecoration(ingredientDivider)
        binding.rvMethod.addItemDecoration(methodDivider)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                binding.tVPortionNum.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    private fun createDivider(): MaterialDividerItemDecoration {
        return MaterialDividerItemDecoration(
            requireContext(),
            LinearLayoutManager.VERTICAL
        ).apply {
            setDividerInsetStartResource(requireContext(), R.dimen.divider_inset)
            setDividerInsetEndResource(requireContext(), R.dimen.divider_inset)

            dividerThickness = resources.getDimensionPixelSize(R.dimen.divider_thickness)
            dividerColor = resources.getColor(R.color.color_divider, null)
            isLastItemDecorated = false
        }
    }

    private fun saveFavorites(favoriteIds: Set<String>) {
        sharedPrefs.edit()
            .putStringSet(KEY_FAVORITE_RECIPES, favoriteIds)
            .apply()
    }

    private fun getFavorites(): MutableSet<String> {
        val savedFavorites = sharedPrefs.getStringSet(KEY_FAVORITE_RECIPES, emptySet())
        return HashSet(savedFavorites ?: emptySet())
    }
}