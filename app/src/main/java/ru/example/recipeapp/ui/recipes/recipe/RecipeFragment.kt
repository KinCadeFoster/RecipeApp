package ru.example.recipeapp.ui.recipes.recipe

import android.os.Build
import android.os.Bundle
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

    private val recipeId: Int? by lazy {
        val recipe: Recipe? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(Constants.ARG_RECIPE, Recipe::class.java)
        } else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable(Constants.ARG_RECIPE)
        }
        recipe?.id
    }
    private val viewModel: RecipeViewModel by viewModels {
        RecipeViewModelFactory(requireContext(), recipeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initRecycler()

        viewModel.recipeLiveData.observe(viewLifecycleOwner) { stateRecipe ->
            stateRecipe.recipe?.let { recipe ->
                updateUIWithRecipe(stateRecipe)
            } ?: run {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_recipe_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateUIWithRecipe(stateRecipe: RecipeState) {
        binding.tVHeader.text = stateRecipe.recipe?.title
        updateFavoriteIcon(stateRecipe.isFavorite)
        binding.tVPortionNum.text = stateRecipe.portionCount.toString()
        binding.headerImage.setImageDrawable(stateRecipe.recipeImage)
        binding.headerImage.contentDescription = getString(
            R.string.content_description_recipe_image, stateRecipe.recipe?.title
        )
        stateRecipe.recipe?.let { ingredientsAdapter.updateDataSet(it.ingredients) }
        stateRecipe.recipe?.let { (binding.rvMethod.adapter as MethodAdapter).updateDataSet(it.method) }
    }

    private fun initUI() {
        binding.imageHeartButton.setOnClickListener {
            viewModel.onFavoritesClicked()
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val iconResId = if (isFavorite) R.drawable.ic_heart else R.drawable.ic_heart_empty
        binding.imageHeartButton.setImageResource(iconResId)
    }

    private fun initRecycler() {
        binding.rvIngredients.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMethod.layoutManager = LinearLayoutManager(requireContext())

        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvMethod.adapter = MethodAdapter(emptyList())

        val ingredientDivider = createDivider()
        val methodDivider = createDivider()

        binding.rvIngredients.addItemDecoration(ingredientDivider)
        binding.rvMethod.addItemDecoration(methodDivider)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                binding.tVPortionNum.text = progress.toString()
                viewModel.updatePortions(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
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
}