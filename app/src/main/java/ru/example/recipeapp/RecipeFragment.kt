package ru.example.recipeapp

import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.example.recipeapp.databinding.FragmentRecipeBinding
import ru.example.recipeapp.model.Recipe


class RecipeFragment : Fragment() {
    private val binding by lazy {
        FragmentRecipeBinding.inflate(layoutInflater)
    }
    private var isFavorite: Boolean = false
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = getRecipeFromArguments()

        if (recipe != null) {
            initUI(recipe)
            initRecycler(recipe)
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

        binding.imageHeartButton.setOnClickListener {
            isFavorite = !isFavorite
            if (isFavorite) {
                binding.imageHeartButton.setImageResource(R.drawable.ic_heart)
            } else {
                binding.imageHeartButton.setImageResource(R.drawable.ic_heart_empty)
            }
        }
    }


    private fun initRecycler(recipe: Recipe) {
        ingredientsAdapter = IngredientsAdapter(recipe.ingredients)

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
}