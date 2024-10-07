package ru.example.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.example.recipeapp.databinding.FragmentRecipeBinding
import ru.example.recipeapp.model.Ingredient


class RecipeFragment : Fragment() {
    private var recipeId: Int? = null
    private var recipeName: String? = null
    private var recipeImageUrl: String? = null
    private var recipeMethod: List<String>? = null
    private var recipeIngredients: List<Ingredient>? = null

    private val binding by lazy {
        FragmentRecipeBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipeId = arguments?.getInt("ARG_RECIPE_ID")
        recipeName = arguments?.getString("ARG_RECIPE_NAME")

        binding.textView1.text = recipeId.toString()
        binding.textView2.text = recipeName

    }


}