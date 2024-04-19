package com.sample.ripedotnet.core.data.models

import com.sample.ripedotnet.core.database.model.InetNumEntity
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.network.ext.getFlagUrl
import com.sample.ripedotnet.core.network.models.NetworkRipeObject
import com.sample.ripedotnet.core.network.models.common.Object
import com.sample.ripedotnet.core.network.models.keys.AttrNames
import com.sample.ripedotnet.core.network.models.keys.ObjectType
import java.util.UUID

private val String.toTrimUUID: String
    get() = UUID.nameUUIDFromBytes(trim().toByteArray()).toString()

internal fun InetNum.toInetNumEntity() = InetNumEntity(
    inetId = ip.toTrimUUID,
    orgId = orgId,
    ip = ip,
    name = name,
    country = country,
    ipFrom = ipRange?.first,
    ipTo = ipRange?.second,
    descr = descr,
    queryIp = null
)

internal fun NetworkRipeObject.networkToInetNumEntities(): List<InetNumEntity> {
    return objects.objectX
        .asSequence()
        .filter { it.type == ObjectType.INETNUM.type }
        .mapNotNull(::mapToInetNumEntity)
        .toList()
}

internal fun NetworkRipeObject.networkToInetNumModels(): List<InetNum> {
    return objects.objectX
        .asSequence()
        .filter { it.type == ObjectType.INETNUM.type }
        .mapNotNull(::mapToInetNumModel)
        .toList()
}

fun InetNumEntity.toInetNumModel() = InetNum(
    id = inetId,
    ip = ip,
    orgId = orgId,
    name = name,
    country = country,
    ipRange = ipFrom?.let { Pair(it, ipTo) },
    descr = descr,
    countryFlagUrl = getFlagUrl(country)
)

fun List<InetNumEntity>.toInetNumModels() = map { it.toInetNumModel() }

private fun mapToInetNumEntity(obj: Object): InetNumEntity? {
    val ip = obj.primaryKey.attribute.getByKey(key = ObjectType.INETNUM.type)?.value ?: return null
    val splitIp = ip.split("-").takeIf { it.isNotEmpty() }?.let { Pair(it[0], it.getOrNull(1)) }
    return InetNumEntity(
        inetId = ip.toTrimUUID,
        ip = obj.primaryKey.attribute.getByKey(key = ObjectType.INETNUM.type)?.value ?: return null,
        name = obj.attributes.attribute.joinByKey(key = AttrNames.NET_NAME.key) ?: return null,
        country = obj.attributes.attribute.getByKey(key = AttrNames.COUNTRY.key)?.value,
        orgId = obj.resourceHolder?.key,
        descr = obj.attributes.attribute.joinByKey(key = AttrNames.DESCR.key),
        ipFrom = splitIp?.first,
        ipTo = splitIp?.second,
        queryIp = null,
    )
}

private fun mapToInetNumModel(obj: Object): InetNum? {
    val ip = obj.primaryKey.attribute.getByKey(key = ObjectType.INETNUM.type)?.value ?: return null
    val splitIp = ip.split("-").takeIf { it.isNotEmpty() }?.let { Pair(it[0], it.getOrNull(1)) }
    val country = obj.attributes.attribute.getByKey(key = AttrNames.COUNTRY.key)?.value
    return InetNum(
        id = ip.toTrimUUID,
        ip = obj.primaryKey.attribute.getByKey(key = ObjectType.INETNUM.type)?.value ?: return null,
        name = obj.attributes.attribute.joinByKey(key = AttrNames.NET_NAME.key) ?: return null,
        country = country,
        orgId = obj.resourceHolder?.key,
        descr = obj.attributes.attribute.joinByKey(key = AttrNames.DESCR.key),
        ipRange = splitIp,
        countryFlagUrl = getFlagUrl(country)
    )
}
