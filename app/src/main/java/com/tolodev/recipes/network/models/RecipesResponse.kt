package com.tolodev.recipes.network.models

import com.squareup.moshi.Json
import com.tolodev.recipes.extensions.orZero

data class RecipesResponse(
    @Json(name = "sys") val sys: Sys,
    @Json(name = "total") val total: Int,
    @Json(name = "skip") val skip: Int,
    @Json(name = "limit") val limit: Int,
    @Json(name = "items") val _items: List<Item>?,
    @Json(name = "includes") val _includes: Map<Any, List<Item>>?
){
    val items: List<Item>
        get() = _items.orEmpty()

    val includes: Map<Any, List<Item>>
        get() = _includes.orEmpty()
}

data class Item(
    @Json(name = "metadata") val metadata: Metadata,
    @Json(name = "sys") val sys: Sys,
    @Json(name = "fields") val fields: Fields
)

data class Metadata(@Json(name = "tags") val tags: List<Any>)

data class Sys(
    @Json(name = "id") val _id: String?,
    @Json(name = "type") val _type: String?,
    @Json(name = "linkType") val _linkType: String?,
    @Json(name = "createdAt") val _createdAt: String?,
    @Json(name = "updatedAt") val _updatedAt: String?,
    @Json(name = "environment") val environment: SysData?,
    @Json(name = "revision") val _revision: Int?,
    @Json(name = "locale") val _locale: String?,
    @Json(name = "contentType") val contentType: SysData?,
    @Json(name = "space") val space: SysData?
){

    val id: String
        get() = _id.orEmpty()

    val type: String
        get() = _type.orEmpty()

    val linkType: String
        get() = _linkType.orEmpty()

    val createdAt: String
        get() = _createdAt.orEmpty()

    val updatedAt: String
        get() = _updatedAt.orEmpty()

    val revision: Int
        get() = _revision.orZero()

    val locale: String
        get() = _locale.orEmpty()
}

data class SysData(@Json(name = "sys") val sys: Sys)

data class Fields(
    @Json(name = "title") val _title: String?,
    @Json(name = "calories") val _calories: Int?,
    @Json(name = "description") val _description: String?,
    @Json(name = "photo") val photo: Photo?,
    @Json(name = "chef") val chef: SysData?,
    @Json(name = "tags") val _tags: List<SysData>?,
    @Json(name = "file") val file: File?,
    @Json(name = "name") val _name: String?
){

    val title: String
        get() = _title.orEmpty()

    val calories: Int
        get() = _calories.orZero()

    val description: String
        get() = _description.orEmpty()

    val tags: List<SysData>
        get() = _tags.orEmpty()

    val name: String
        get() = _name.orEmpty()
}

data class Photo(@Json(name = "sys") val sys: Sys)

data class File(
    @Json(name = "url") val url: String,
    @Json(name = "fileName") val fileName: String,
    @Json(name = "contentType") val contentType: String,
    @Json(name = "details") val details: FileDetails
)

data class FileDetails(
    @Json(name = "size") val size: Int,
    @Json(name = "image") val image: FileImage
)

data class FileImage(
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int
)
