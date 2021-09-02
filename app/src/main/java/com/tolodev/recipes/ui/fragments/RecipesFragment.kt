package com.tolodev.recipes.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.tolodev.recipes.databinding.FragmentRecipesBinding
import com.tolodev.recipes.extensions.safeNavigate
import com.tolodev.recipes.ui.adapters.RecipesAdapter
import com.tolodev.recipes.ui.models.Recipe
import com.tolodev.recipes.ui.viewModel.RecipesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesFragment : Fragment() {

    private var binding: FragmentRecipesBinding? = null

    private val recipesViewModel by viewModels<RecipesViewModel>()

    private val recipesAdapter: RecipesAdapter by lazy {
        RecipesAdapter {
            showRecipeDetail(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRecipesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribe()
    }

    private fun initViews() {
        binding?.recyclerViewRecipes?.run {
            adapter = recipesAdapter
            layoutManager = GridLayoutManager(this@RecipesFragment.context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (position == 0) 2 else 1
                    }

                }
            }
        }
    }

    private fun subscribe() {
        recipesViewModel.observeRecipes().observe(viewLifecycleOwner, { showRecipes(it) })
    }

    private fun showRecipes(recipes: List<Recipe>) {
        binding?.recyclerViewRecipes?.adapter = recipesAdapter
        recipesAdapter.setRecipes(recipes)
    }

    private fun showRecipeDetail(recipe: Recipe) {
        NavHostFragment.findNavController(this)
            .safeNavigate(
                RecipesFragmentDirections.actionRecipesFragmentToRecipeDetailFragment(
                    recipe
                )
            )
    }
}