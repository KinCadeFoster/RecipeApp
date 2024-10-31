package ru.example.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(var dataSet: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = "${position + 1}. ${dataSet[position]}"
        holder.binding.tvStep.text = text
    }

    override fun getItemCount() = dataSet.size

    fun updateDataSet(newMethods: List<String>) {
        this.dataSet = newMethods
        notifyDataSetChanged()
    }
}