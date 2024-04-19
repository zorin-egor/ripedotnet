package com.sample.ripedotnet.feature.inetnum_by_ip.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.sample.ripedotnet.core.designsystem.icon.Icons
import com.sample.ripedotnet.core.model.logic.InetNum
import com.sample.ripedotnet.core.ui.ext.BorderCircleImageRequest
import com.sample.ripedotnet.core.ui.widgets.ImageLoadingWidget
import com.sample.ripedotnet.feature.inetnum_by_ip.R

@Composable
fun InetNumByIpContent(
    inetNum: InetNum,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val orgId = inetNum.orgId
    val materialTheme = MaterialTheme.colorScheme

    val isError = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(true) }
    val imageLoader = rememberAsyncImagePainter(
        BorderCircleImageRequest(
            url = inetNum.countryFlagUrl,
            isError = isError,
            isLoading = isLoading,
            strokeColor = materialTheme.onSurfaceVariant,
            strokeWidth = 1.dp
        ).build()
    )

    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(top = 32.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
        ) {

            ImageLoadingWidget(
                painter = imageLoader,
                isLoading = isLoading.value,
                isError = isError.value,
                placeHolder = Icons.FlagCircle,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_inet_num_by_ip_organization_id)
                        .plus(":"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = inetNum.orgId
                        ?: stringResource(id = R.string.feature_inet_num_by_ip_none),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_inet_num_by_ip_organization_name)
                        .plus(":"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = inetNum.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_inet_num_by_ip_organization_ip_addresses)
                        .plus(":"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = inetNum.ip,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.feature_inet_num_by_ip_organization_descr)
                        .plus(":"),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = inetNum.descr
                        ?: stringResource(id = R.string.feature_inet_num_by_ip_none),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.weight(1.0f))

            if (orgId != null) {
                Button(
                    onClick = { onItemClick(orgId) },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(id = R.string.feature_inet_num_by_ip_organization_get_networks),
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.wrapContentSize(),
                    )
                }
            } else {
                Text(
                    text = stringResource(id = R.string.feature_inet_num_by_ip_organization_id_not_found),
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}