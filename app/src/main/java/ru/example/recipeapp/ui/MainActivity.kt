package ru.example.recipeapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.example.recipeapp.databinding.ActivityMainBinding
import ru.example.recipeapp.ui.category.CategoriesListFragment
import ru.example.recipeapp.ui.recipes.favorites.FavoritesFragment


class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<CategoriesListFragment>(binding.mainContainer.id)
            }
        }

        binding.bCategories.setOnClickListener {
            replaceFragment<CategoriesListFragment>()
        }

        binding.bFavorites.setOnClickListener {
            replaceFragment<FavoritesFragment>()
        }
    }

    private inline fun <reified T : Fragment> replaceFragment() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace<T>(binding.mainContainer.id)
        }
    }
}