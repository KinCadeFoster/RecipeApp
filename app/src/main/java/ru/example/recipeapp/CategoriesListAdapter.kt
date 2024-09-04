package ru.example.recipeapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemCategoryBinding
import ru.example.recipeapp.model.Category

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvTitle.text = category.title
            binding.tvDescription.text = category.description

            try {
                val drawable = Drawable.createFromStream(
                    binding.root.context.assets.open(category.imageUrl), null
                )
                binding.image.setImageDrawable(drawable)

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category: Category = dataSet[position]
        holder.bind(category)
    }

    override fun getItemCount() = dataSet.size
}