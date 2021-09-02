package com.tolodev.recipes.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tolodev.recipes.databinding.ItemRecipeViewBinding
import com.tolodev.recipes.ui.models.Recipe


class RecipesAdapter(private val action: (recipe: Recipe) -> Unit) :
    RecyclerView.Adapter<RecipesAdapter.ItemRecipeViewHolder>() {

    private var recipes: List<Recipe> = ArrayList()

    fun setRecipes(recipes: List<Recipe>) {
        this.recipes = recipes
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemRecipeViewHolder {
        val binding = ItemRecipeViewBinding.inflate(LayoutInflater.from(parent.context)).apply {
            root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return ItemRecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemRecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }

    override fun getItemCount(): Int = recipes.size

    inner class ItemRecipeViewHolder(private val binding: ItemRecipeViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            with(binding) {
                Glide.with(binding.root.context).load(recipe.photoUrl).into(imageViewRecipePhoto)
                textViewRecipeTitle.text = recipe.title
                root.setOnClickListener { action.invoke(recipe) }
            }
        }
    }
}
