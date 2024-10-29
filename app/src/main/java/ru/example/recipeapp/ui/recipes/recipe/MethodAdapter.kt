package ru.example.recipeapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(private var method: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val text = "${position + 1}. ${method[position]}"
        holder.binding.tvStep.text = text
    }

    override fun getItemCount() = method.size

    fun updateDataSet(newMethods: List<String>) {
        this.method = newMethods
        notifyDataSetChanged()
    }
}