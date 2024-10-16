package ru.example.recipeapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.example.recipeapp.databinding.ItemMethodBinding

class MethodAdapter(private val method: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvStep.text = method[position]
    }

    override fun getItemCount() = method.size
}