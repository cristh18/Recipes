package com.tolodev.recipes.network.api

import com.tolodev.recipes.network.RecipesService
import com.tolodev.recipes.network.models.RecipesResponse
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.annotations.CheckReturnValue
import javax.inject.Inject

class RecipesApi @Inject constructor(
    private val services: RecipesService
) : BaseApi() {

    @CheckReturnValue
    fun getEntries(contentType: String, scheduler: Scheduler? = null): Single<RecipesResponse> =
        subscribe(services.getEntries(contentType), scheduler)
}
