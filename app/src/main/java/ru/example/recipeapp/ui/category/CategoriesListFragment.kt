package ru.example.recipeapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.example.recipeapp.R
import ru.example.recipeapp.databinding.FragmentListCategoriesBinding
import ru.example.recipeapp.model.Category
import ru.example.recipeapp.ui.Constants
import ru.example.recipeapp.ui.recipes.recipe_list.RecipesListFragment

class CategoriesListFragment : Fragment() {

    private val binding by lazy {
        FragmentListCategoriesBinding.inflate(layoutInflater)
    }

    private val viewModel: CategoriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            initRecycler(categories)
        }
    }

    private fun initRecycler(categories: List<Category>) {
        val categoriesAdapter = CategoriesListAdapter(categories)
        binding.rvCategories.adapter = categoriesAdapter

        categoriesAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId, categories)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int, categories: List<Category>) {
        categories.find { it.id == categoryId }?.let { category ->
            val categoryName = category.title
            val categoryImageUrl = category.imageUrl

            val bundle = bundleOf(
                Constants.ARG_CATEGORY_ID to category.id,
                Constants.ARG_CATEGORY_NAME to categoryName,
                Constants.ARG_CATEGORY_IMAGE_URL to categoryImageUrl,
            )
            parentFragmentManager.commit {
                setReorderingAllowed(true)
                replace<RecipesListFragment>(R.id.mainContainer, args = bundle)
                addToBackStack(null)
            }
        }
    }
}