package ru.example.recipeapp

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.example.recipeapp.databinding.FragmentListRecipesBinding
import ru.example.recipeapp.model.STUB


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
        val recipeAdapter = RecipeAdapter(STUB.getRecipesByCategoryId(categoryId!!))
        binding.rvRecipes.adapter = recipeAdapter

        recipeAdapter.setOnItemClickListener(object :
            RecipeAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        STUB.getRecipesByCategoryId(categoryId!!).find { it.id == recipeId }?.let { recipe ->
            val recipeName = recipe.title
            val recipeImageUrl = recipe.imageUrl
            val ingredients = recipe.ingredients
            val method = recipe.method

            val bundle = bundleOf(
                Constants.ARG_RECIPE_ID to recipe.id,
                Constants.ARG_RECIPE_NAME to recipeName,
                Constants.ARG_RECIPE_IMAGE_URL to recipeImageUrl,
                Constants.ARG_RECIPE_INGREDIENTS to ingredients,
                Constants.ARG_RECIPE_METHOD to method
            )
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecipeFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        }
    }
}