package ru.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemIngredientBinding
import ru.example.recipeapp.model.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

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

        val quantityValue = ingredient.quantity.toDoubleOrNull() ?: 0.0
        val totalQuantity = quantityValue * quantity

        val quantityText = if (totalQuantity % 1 == 0.0) {
            "${totalQuantity.toInt()} ${ingredient.unitOfMeasure}"
        } else {
            "${"%.1f".format(totalQuantity)} ${ingredient.unitOfMeasure}"
        }
        holder.binding.tvQuantity.text = quantityText
    }

    override fun getItemCount() = dataSet.size

    fun updateIngredients(progress: Int) {
        quantity = progress.coerceAtLeast(1)
        notifyDataSetChanged()
    }
}