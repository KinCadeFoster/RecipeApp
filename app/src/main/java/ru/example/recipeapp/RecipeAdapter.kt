package ru.example.recipeapp

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemRecipeBinding
import ru.example.recipeapp.model.Recipe

class RecipeAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe: Recipe = dataSet[position]
        holder.binding.tvTitle.text = recipe.title

        try {
            val drawable = Drawable.createFromStream(
                holder.binding.root.context.assets.open(recipe.imageUrl), null
            )
            holder.binding.image.setImageDrawable(drawable)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        holder.binding.root.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }

    override fun getItemCount() = dataSet.size
}