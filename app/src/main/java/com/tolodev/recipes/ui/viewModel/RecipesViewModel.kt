package com.tolodev.recipes.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tolodev.recipes.network.api.RecipesApi
import com.tolodev.recipes.network.models.RecipesResponse
import com.tolodev.recipes.ui.models.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(recipesApi: RecipesApi) : ViewModel() {

    private val disposables = CompositeDisposable()

    private val _recipes = MutableLiveData<List<Recipe>>()

    init {
        disposables.add(
            recipesApi.getEntries("recipe").subscribe({
                _recipes.postValue(getRecipes(it))
            },
                {
                    Timber.e(it, "Error fetching recipes")
                })
        )
    }

    private fun getRecipes(recipesResponse: RecipesResponse): List<Recipe> {
        val includes = recipesResponse.includes
        return recipesResponse.items.map {
            Recipe(includes, it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }

    fun observeRecipes(): LiveData<List<Recipe>> = _recipes
}