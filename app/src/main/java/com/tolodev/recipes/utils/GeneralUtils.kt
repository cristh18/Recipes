package com.tolodev.recipes.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.WindowManager

fun hasInternetConnection(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

val Context.isAnimatorEnabled: Boolean
    get() = Settings.Global.getFloat(
        contentResolver,
        Settings.Global.ANIMATOR_DURATION_SCALE,
        1f
    ) == 1f
