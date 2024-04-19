package com.sample.ripedotnet.feature.organizations_by_name.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility
import coil.compose.rememberAsyncImagePainter
import com.sample.ripedotnet.core.model.logic.Organization
import com.sample.ripedotnet.core.ui.ext.BorderCircleImageRequest

@Composable
fun OrganizationItemContent(
    organization: Organization,
    onOrganizationClick: (Organization) -> Unit,
    modifier: Modifier
) {
    val materialTheme = MaterialTheme.colorScheme
    val isLoading = remember { mutableStateOf(true) }
    val isError = remember { mutableStateOf(false) }
    val imageLoader = rememberAsyncImagePainter(
        BorderCircleImageRequest(
            url = organization.countryFlagUrl,
            isError = isError,
            isLoading = isLoading,
            strokeColor = materialTheme.onSurfaceVariant,
            strokeWidth = 1.dp
        ).build()
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true),
                    onClick = { onOrganizationClick(organization) },
                )
        ) {
            val (image, name, orgId, address) = createRefs()
            val verticalChain = createVerticalChain(name, orgId, address, chainStyle = ChainStyle.Packed)

            Text(
                text = organization.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                minLines = 1,
                maxLines = 2,
                textAlign = TextAlign.Start,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 20.sp,
                modifier = Modifier.constrainAs(name) {
                    height = Dimension.wrapContent
                    width = Dimension.fillToConstraints
                    top.linkTo(anchor = parent.top)
                    start.linkTo(anchor = parent.start, margin = 8.dp)
                    end.linkTo(anchor = image.start, margin = 8.dp)
                    bottom.linkTo(anchor = orgId.top)
                }.padding(bottom = 8.dp, start = 8.dp, top = 12.dp),
            )

            Text(
                text = organization.id,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                maxLines = 1,
                fontWeight = FontWeight.Medium,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(orgId) {
                    height = Dimension.wrapContent
                    width = Dimension.fillToConstraints
                    top.linkTo(anchor = name.bottom)
                    start.linkTo(anchor = name.start)
                    end.linkTo(anchor = name.end)
                    bottom.linkTo(anchor = address.top)
                }.padding(bottom = 4.dp, start = 8.dp, top = 0.dp, end = 0.dp),
            )

            Text(
                text = organization.address,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                minLines = 1,
                maxLines = 2,
                fontWeight = FontWeight.Light,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.constrainAs(address) {
                    height = Dimension.wrapContent
                    width = Dimension.fillToConstraints
                    top.linkTo(anchor = orgId.bottom)
                    start.linkTo(anchor = name.start)
                    end.linkTo(anchor = name.end)
                    bottom.linkTo(anchor = parent.bottom)
                }.padding(bottom = 12.dp, start = 8.dp),
            )

            Image(
                painter = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                modifier = Modifier
                    .constrainAs(image) {
                        height = Dimension.wrapContent
                        width = Dimension.wrapContent
                        end.linkTo(anchor = parent.end, margin = 8.dp)
                        top.linkTo(anchor = parent.top)
                        bottom.linkTo(anchor = parent.bottom)
                        visibility = if (organization.countryFlagUrl != null)
                            Visibility.Visible else Visibility.Gone
                    }
                    .padding(end = 8.dp)
                    .size(48.dp)
            )
        }
    }
}