package com.sample.ripedotnet.feature.inetnum_by_ip

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.ripedotnet.core.common.extensions.isValidIpFormat
import com.sample.ripedotnet.core.designsystem.component.CircularContent
import com.sample.ripedotnet.core.designsystem.component.SearchToolbar
import com.sample.ripedotnet.core.ui.widgets.SimplePlaceholderContent
import com.sample.ripedotnet.feature.inetnum_by_ip.widgets.InetNumByIpContent
import timber.log.Timber
import com.sample.ripedotnet.core.ui.R as CoreUiR

@Composable
internal fun InetNumByIpScreen(
    onItemClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: InetNumByIpViewModel = hiltViewModel()
) {
    Timber.d("InetNumByIpScreen()")

    val inetNumByIpState: InetNumByIpUiState by viewModel.state.collectAsStateWithLifecycle()
    val inetNumByIpAction: InetNumByIpActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = inetNumByIpAction) {
        is InetNumByIpActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Timber.d("InetNumByIpScreen() - state: $inetNumByIpState, $inetNumByIpAction")

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        SearchToolbar(
            searchQuery = inetNumByIpState.query,
            contentDescriptionBack = "contentDescriptionBack",
            contentDescriptionSearch = "contentDescriptionSearch",
            contentDescriptionClose = "contentDescriptionClose",
            onSearchQueryChanged = viewModel::queryInetNumByIp,
            onSearchTriggered = {},
            modifier = Modifier.wrapContentHeight(),
            placeholder = R.string.feature_inet_num_by_ip_search_title,
            isFocusRequest = false,
            inputFilter = { it.isValidIpFormat },
            keyboardType = KeyboardType.Number
        )

        val placeHolderModifier = Modifier
            .fillMaxWidth()
            .weight(1.0f)

        when (val state = inetNumByIpState.state) {
            InetNumByIpUiStates.Start -> SimplePlaceholderContent(
                header = CoreUiR.string.search_placeholder_header,
                title = CoreUiR.string.search_placeholder_title,
                image = CoreUiR.drawable.ic_baseline_search_78,
                imageContentDescription = CoreUiR.string.search_placeholder_title,
                modifier = placeHolderModifier
            )
            InetNumByIpUiStates.Loading -> CircularContent()
            InetNumByIpUiStates.Empty -> SimplePlaceholderContent(
                header = CoreUiR.string.empty_placeholder_header,
                title = CoreUiR.string.empty_placeholder_title,
                image = CoreUiR.drawable.ic_baseline_cached_78,
                imageContentDescription = CoreUiR.string.empty_placeholder_header,
                modifier = placeHolderModifier
            )
            is InetNumByIpUiStates.Success -> InetNumByIpContent(
                inetNum = state.inetNum,
                onItemClick = onItemClick,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = viewModel::getSelfIp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.feature_inet_num_by_ip_check_self_ip),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.wrapContentSize(),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}