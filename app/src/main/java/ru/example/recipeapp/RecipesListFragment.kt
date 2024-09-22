package ru.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.example.recipeapp.databinding.FragmentListRecipesBinding


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

        binding.tvId.text = categoryId.toString()
        binding.tvName.text = categoryName
        binding.tvURL.text = categoryImageUrl
    }
}