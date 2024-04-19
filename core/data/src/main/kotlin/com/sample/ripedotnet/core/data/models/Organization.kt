package com.sample.ripedotnet.core.data.models

import com.sample.ripedotnet.core.database.model.OrganizationsEntity
import com.sample.ripedotnet.core.model.logic.Organization
import com.sample.ripedotnet.core.network.ext.getFlagUrl
import com.sample.ripedotnet.core.network.models.NetworkRipeObject
import com.sample.ripedotnet.core.network.models.common.Object
import com.sample.ripedotnet.core.network.models.keys.AttrNames
import com.sample.ripedotnet.core.network.models.keys.ObjectType


internal fun Organization.toOrganizationEntity() = OrganizationsEntity(
    orgId = id,
    name = name,
    country = country,
    address = address,
    created = created,
    modified = modified,
    phone = phone,
    fax = fax,
)

internal fun List<Organization>.toOrganizationsEntities() = map { it.toOrganizationEntity() }

internal fun NetworkRipeObject.networkToOrganizationsEntities(): List<OrganizationsEntity> {
    return objects.objectX
        .asSequence()
        .filter { it.type == ObjectType.ORG.type }
        .mapNotNull(::mapToOrgEntity)
        .toList()
}

internal fun NetworkRipeObject.networkToOrganizationModel(): List<Organization> {
    return objects.objectX
        .asSequence()
        .filter { it.type == ObjectType.ORG.type }
        .mapNotNull(::mapToOrgModel)
        .toList()
}

internal fun OrganizationsEntity.toOrgModel() = Organization(
    id = orgId,
    name = name,
    country = country,
    address = address,
    created = created,
    modified = modified,
    phone = phone,
    fax = fax,
    countryFlagUrl = getFlagUrl(country)
)

fun List<OrganizationsEntity>.toOrgModels() = map { it.toOrgModel() }

private fun mapToOrgEntity(obj: Object): OrganizationsEntity? {
    return OrganizationsEntity(
        orgId = obj.primaryKey.attribute.getByKey(key = ObjectType.ORG.type)?.value ?: return null,
        name = obj.attributes.attribute.joinByKey(key = AttrNames.ORG_NAME.key) ?: return null,
        address = obj.attributes.attribute.joinByKey(key = AttrNames.ADDRESS.key) ?: return null,
        created = obj.attributes.attribute.getByKey(key = AttrNames.CREATED.key)?.value ?: return null,
        country = obj.attributes.attribute.getByKey(key = AttrNames.COUNTRY.key)?.value,
        modified = obj.attributes.attribute.getByKey(key = AttrNames.MODIFIED.key)?.value,
        phone = obj.attributes.attribute.getByKey(key = AttrNames.PHONE.key)?.value,
        fax = obj.attributes.attribute.getByKey(key = AttrNames.FAX_NO.key)?.value
    )
}

private fun mapToOrgModel(obj: Object): Organization? {
    val country = obj.attributes.attribute.getByKey(key = AttrNames.COUNTRY.key)?.value
    return Organization(
        id = obj.primaryKey.attribute.getByKey(key = ObjectType.ORG.type)?.value ?: return null,
        name = obj.attributes.attribute.joinByKey(key = AttrNames.ORG_NAME.key) ?: return null,
        address = obj.attributes.attribute.joinByKey(key = AttrNames.ADDRESS.key) ?: return null,
        created = obj.attributes.attribute.getByKey(key = AttrNames.CREATED.key)?.value ?: return null,
        country = country,
        modified = obj.attributes.attribute.getByKey(key = AttrNames.MODIFIED.key)?.value,
        phone = obj.attributes.attribute.getByKey(key = AttrNames.PHONE.key)?.value,
        fax = obj.attributes.attribute.getByKey(key = AttrNames.FAX_NO.key)?.value,
        countryFlagUrl = getFlagUrl(country)
    )
}
