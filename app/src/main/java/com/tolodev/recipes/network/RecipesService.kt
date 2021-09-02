package com.tolodev.recipes.network

import com.tolodev.recipes.network.models.RecipesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipesService {

    @GET("entries")
    fun getEntries(@Query("content_type") contentType: String): Single<RecipesResponse>
}
