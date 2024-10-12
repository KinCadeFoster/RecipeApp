package ru.example.recipeapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ru.example.recipeapp.databinding.FragmentRecipeBinding
import ru.example.recipeapp.model.Recipe


class RecipeFragment : Fragment() {
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

        val recipe = getRecipeFromArguments()

        if (recipe != null) {
            binding.textView1.text = recipe.title
        } else {
            Toast.makeText(requireContext(), "Не нашли такого рецепта", Toast.LENGTH_SHORT).show()
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
}


