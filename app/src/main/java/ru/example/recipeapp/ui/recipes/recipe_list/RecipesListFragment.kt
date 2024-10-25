package ru.example.recipeapp.ui.recipes.recipe_list

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.example.recipeapp.R
import ru.example.recipeapp.databinding.FragmentListRecipesBinding
import ru.example.recipeapp.data.STUB
import ru.example.recipeapp.ui.Constants
import ru.example.recipeapp.ui.recipes.recipe.RecipeFragment


class RecipesListFragment : Fragment() {
    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null

    private val binding by lazy {
        FragmentListRecipesBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryId = arguments?.getInt("ARG_CATEGORY_ID")
        categoryName = arguments?.getString("ARG_CATEGORY_NAME")
        categoryImageUrl = arguments?.getString("ARG_CATEGORY_IMAGE_URL")


        binding.tvName.text = categoryName
        categoryImageUrl?.let {
            val imageResources = requireContext().assets.open(it).use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
            binding.ivCategoryImage.setImageBitmap(imageResources)
        }
        initRecycler()
    }

    private fun initRecycler() {
        val recipes = categoryId?.let { STUB.getRecipesByCategoryId(it) } ?: emptyList()
        val recipeAdapter = RecipeAdapter(recipes)
        binding.rvRecipes.adapter = recipeAdapter

        recipeAdapter.setOnItemClickListener(object :
            RecipeAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
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