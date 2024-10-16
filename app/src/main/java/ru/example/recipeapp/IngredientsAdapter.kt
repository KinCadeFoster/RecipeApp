package ru.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemIngredientBinding
import ru.example.recipeapp.model.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ingredient: Ingredient = dataSet[position]
        holder.binding.tvIngredient.text = ingredient.description
        val quantity = "${ingredient.quantity} ${ingredient.unitOfMeasure}"
        holder.binding.tvQuantity.text = quantity
    }

    override fun getItemCount() = dataSet.size
}