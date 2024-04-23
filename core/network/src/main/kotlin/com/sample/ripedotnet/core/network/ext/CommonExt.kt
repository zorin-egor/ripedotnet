package com.sample.ripedotnet.core.network.ext

private const val FLAG_PLACEHOLDER = "<COUNTRY_CODE>"
private const val FLAG_SIZE_PLACEHOLDER = "<FLAG_SIZE>"
private const val FLAG_SIZE = 64
private const val FLAG_URL = "https://flagsapi.com/$FLAG_PLACEHOLDER/flat/$FLAG_SIZE_PLACEHOLDER.png"

fun getFlagUrl(code: String?, size: Int = FLAG_SIZE): String? =
    code?.let {
        FLAG_URL.replace(FLAG_PLACEHOLDER, it)
            .replace(FLAG_SIZE_PLACEHOLDER, size.toString())
    }