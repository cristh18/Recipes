package com.tolodev.recipes.extensions

import androidx.navigation.NavController
import androidx.navigation.NavDirections

fun Int?.orZero() = this ?: 0

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}