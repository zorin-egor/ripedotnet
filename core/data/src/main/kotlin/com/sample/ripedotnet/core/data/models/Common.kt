package com.sample.ripedotnet.core.data.models

import com.sample.ripedotnet.core.network.models.common.Attribute

internal fun List<Attribute>.getByKey(key: String): Attribute? =
    find { it.name == key }

internal fun List<Attribute>.joinByKey(key: String): String? =
    filter { it.name == key }
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = " ") { it.value }