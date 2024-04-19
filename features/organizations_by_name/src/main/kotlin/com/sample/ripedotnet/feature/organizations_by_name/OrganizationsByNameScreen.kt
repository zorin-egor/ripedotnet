package com.sample.ripedotnet.feature.organizations_by_name

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sample.ripedotnet.core.designsystem.component.CircularContent
import com.sample.ripedotnet.core.designsystem.component.SearchToolbar
import com.sample.ripedotnet.core.ui.widgets.ListContentWidget
import com.sample.ripedotnet.core.ui.widgets.SimplePlaceholderContent
import com.sample.ripedotnet.feature.organizations_by_name.widgets.OrganizationItemContent
import timber.log.Timber

import com.sample.ripedotnet.core.ui.R as CoreUiR

@Composable
internal fun OrganizationsByNameScreen(
    onOrganizationClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    viewModel: OrganizationsByNameViewModel = hiltViewModel(),
) {
    Timber.d("OrganizationsByNameScreen()")

    val organizationsByNameUiState: OrganizationsByNameUiState by viewModel.state.collectAsStateWithLifecycle()
    val organizationsAction: OrganizationsByNameActions? by viewModel.action.collectAsStateWithLifecycle(initialValue = null)

    when(val action = organizationsAction) {
        is OrganizationsByNameActions.ShowError -> {
            LaunchedEffect(key1 = action.error.hashCode()) {
                onShowSnackbar(action.error, null)
            }
        }
        else -> {}
    }

    Column(
         modifier = modifier.fillMaxSize()
    ) {
        SearchToolbar(
            searchQuery = organizationsByNameUiState.query,
            contentDescriptionBack = "contentDescriptionBack",
            contentDescriptionSearch = "contentDescriptionSearch",
            contentDescriptionClose = "contentDescriptionClose",
            onSearchQueryChanged = viewModel::queryOrganizations,
            onSearchTriggered = {},
            modifier = Modifier.wrapContentHeight(),
            placeholder = R.string.feature_organizations_by_name_search_title,
            isFocusRequest = false
        )

        when(val state = organizationsByNameUiState.state) {
            is OrganizationsByNameUiStates.Start -> SimplePlaceholderContent(
                header = CoreUiR.string.search_placeholder_header,
                title = CoreUiR.string.search_placeholder_title,
                image = CoreUiR.drawable.ic_baseline_search_78,
                imageContentDescription = CoreUiR.string.search_placeholder_title
            )
            is OrganizationsByNameUiStates.Loading -> CircularContent()
            is OrganizationsByNameUiStates.Empty -> SimplePlaceholderContent(
                header = CoreUiR.string.empty_placeholder_header,
                title = CoreUiR.string.empty_placeholder_title,
                image = CoreUiR.drawable.ic_baseline_cached_78,
                imageContentDescription = CoreUiR.string.empty_placeholder_header
            )
            is OrganizationsByNameUiStates.Success -> {
                ListContentWidget(
                    items = state.organizations,
                    onKey = { it.id },
                    onItemClick = { onOrganizationClick(it.id) },
                    onBottomEvent = viewModel::nextOrganizations,
                    isBottomProgress = state.isBottomProgress
                ) { user, onClick ->
                    OrganizationItemContent(user, onClick,
                        Modifier.fillMaxWidth()
                            .height(128.dp))
                }
            }
        }
    }
}

