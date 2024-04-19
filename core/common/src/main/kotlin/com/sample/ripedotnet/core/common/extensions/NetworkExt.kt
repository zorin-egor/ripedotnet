package com.sample.ripedotnet.core.common.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.InetAddresses
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Patterns
import androidx.core.text.isDigitsOnly

fun Context.isOnline(): Boolean {
    return (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).let { manager ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = manager.activeNetwork ?: return false
            val networkCapabilities = manager.getNetworkCapabilities(activeNetwork) ?: return false
            when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            manager.activeNetworkInfo.let {
                when (it?.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
}

fun isIpValid(ip: String): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        InetAddresses.isNumericAddress(ip)
    } else {
        Patterns.IP_ADDRESS.matcher(ip).matches()
    }
}

private const val IP_LENGTH = 15
private const val IP_DOTS = 3
private const val IP_MAX = 255

val String.isValidIpFormat: Boolean
    get() = (isDigitsOnly() || contains("."))
        && length <= IP_LENGTH && count { it == '.' } <= IP_DOTS
        && split(".").all { (it.toIntOrNull() ?: 0) <= IP_MAX }