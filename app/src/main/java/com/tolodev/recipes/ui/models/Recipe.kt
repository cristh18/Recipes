package com.tolodev.recipes.ui.models

import android.os.Parcelable
import com.tolodev.recipes.network.models.Item
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(
    val title: String,
    val description: String,
    val photoUrl: String,
    val tags: List<String>,
    val chefName: String
) : Parcelable {

    constructor(includes: Map<Any, List<Item>>, recipe: Item) : this(
        getTitle(recipe),
        getDescription(recipe),
        getPhotoUrl(includes, recipe),
        getTags(includes, recipe),
        getChefName(includes, recipe)
    )
}

fun getTitle(recipe: Item): String {
    return recipe.fields.title
}

fun getDescription(recipe: Item): String {
    return recipe.fields.description
}

fun getPhotoUrl(includes: Map<Any, List<Item>>, recipe: Item): String {
    val assets = includes["Asset"] ?: emptyList()
    val photoId = recipe.fields.photo?.sys?.id.orEmpty()
    val recipeImageUrl = assets.firstOrNull { asset ->
        val assetId = asset.sys.id
        assetId.equals(photoId, true)
    }?.fields?.file?.url.orEmpty()
    return recipeImageUrl.replace(recipeImageUrl, "http:".plus(recipeImageUrl))
}

fun getTags(includes: Map<Any, List<Item>>, recipe: Item): List<String> {
    val entries = includes["Entry"] ?: emptyList()
    val recipeTags = recipe.fields.tags
    val tags = mutableListOf<String>()
    entries.forEach { entry ->
        val entryId = entry.sys.id
        recipeTags.forEach { tagData ->
            val currentTag = tagData.sys.id
            if (entryId.equals(currentTag, true)) {
                tags.add(entry.fields.name.replaceFirstChar { it.titlecase() })
            }
        }
    }
    return tags.toList()
}

fun getChefName(includes: Map<Any, List<Item>>, recipe: Item): String {
    val entries = includes["Entry"] ?: emptyList()
    val chefId = recipe.fields.chef?.sys?.id.orEmpty()
    return entries.firstOrNull { entry ->
        val entryId = entry.sys.id
        entryId.equals(chefId, true)
    }?.fields?.name.orEmpty()
}
