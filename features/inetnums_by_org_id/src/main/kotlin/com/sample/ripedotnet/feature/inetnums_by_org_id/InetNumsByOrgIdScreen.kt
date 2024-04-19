package com.sample.ripedotnet.feature.inetnums_by_org_id

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.ripedotnet.core.designsystem.component.AppTopBar
import com.sample.ripedotnet.core.designsystem.component.CircularContent
import com.sample.ripedotnet.core.designsystem.icon.Icons
import com.sample.ripedotnet.core.ui.widgets.ListContentWidget
import com.sample.ripedotnet.core.ui.widgets.SimplePlaceholderContent
import com.sample.ripedotnet.feature.inetnums_by_org_id.widgets.InetNumsByOrgIdItemContent
import timber.log.Timber

import com.sample.ripedotnet.core.ui.R as CoreUiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun InetNumsByOrgIdScreen(
    onOrganizationClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: InetNumsByOrgIdViewModel = hiltViewModel(),
) {
    Timber.d("InetNumsByOrgIdScreen()")

    val inetNumsByOrgIdUiState: InetNumsByOrgIdUiState by viewModel.state.collectAsStateWithLifecycle()
    val inetNumsByOrgIdAction: InetNumsByOrgIdActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = inetNumsByOrgIdAction) {
        is InetNumsByOrgIdActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            titleRes = R.string.feature_inet_nums_by_org_id_title,
            navigationIcon = Icons.ArrowBack,
            navigationIconContentDescription = stringResource(id = R.string.feature_inet_nums_by_org_id_title),
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.Transparent,
            ),
            onNavigationClick = onBackClick,
        )

        when(val state = inetNumsByOrgIdUiState) {
            InetNumsByOrgIdUiState.Loading -> CircularContent()
            InetNumsByOrgIdUiState.Empty -> SimplePlaceholderContent(
                header = CoreUiR.string.empty_placeholder_header,
                title = CoreUiR.string.empty_placeholder_title,
                image = CoreUiR.drawable.ic_baseline_cached_78,
                imageContentDescription = CoreUiR.string.empty_placeholder_header
            )
            is InetNumsByOrgIdUiState.Success -> {
                ListContentWidget(
                    items = state.inetNums,
                    onKey = { it.id },
                    onItemClick = {

                    },
                    onBottomEvent = viewModel::nextInetNumsByOrgId,
                    isBottomProgress = state.isBottomProgress
                ) { inetNum, onClick ->
                    InetNumsByOrgIdItemContent(inetNum, onClick,
                        Modifier
                            .fillMaxWidth()
                            .height(128.dp))
                }
            }
        }
    }

}

