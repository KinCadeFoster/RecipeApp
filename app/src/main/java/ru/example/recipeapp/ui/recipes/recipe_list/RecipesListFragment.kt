package ru.example.recipeapp.ui.recipes.recipe_list

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
import ru.example.recipeapp.databinding.FragmentListRecipesBinding
import ru.example.recipeapp.model.Category
import ru.example.recipeapp.ui.Constants
import ru.example.recipeapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment : Fragment() {

    private val viewModel: RecipesListViewModel by viewModels()
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
        val categoryId = arguments?.getInt("ARG_CATEGORY_ID")
        val categoryName = arguments?.getString("ARG_CATEGORY_NAME")
        val categoryImageUrl = arguments?.getString("ARG_CATEGORY_IMAGE_URL")

        val category = Category(
            id = categoryId ?: 0,
            title = categoryName ?: "",
            imageUrl = categoryImageUrl ?: "",
            description = ""
        )
        viewModel.loadCategoryData(category, requireContext())

        viewModel.state.observe(viewLifecycleOwner) { state ->
            state.category?.let { category ->
                binding.tvName.text = category.title
            }
            binding.ivCategoryImage.setImageDrawable(state.categoryImage)

            val recipeAdapter = RecipeAdapter(state.recipes)
            binding.rvRecipes.adapter = recipeAdapter
            recipeAdapter.setOnItemClickListener(object : RecipeAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
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