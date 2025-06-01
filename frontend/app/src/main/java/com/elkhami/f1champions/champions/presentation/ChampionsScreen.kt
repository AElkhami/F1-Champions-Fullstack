package com.elkhami.f1champions.champions.presentation

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.elkhami.f1champions.R
import com.elkhami.f1champions.core.ui.theme.F1Red
import com.elkhami.f1champions.core.ui.theme.LocalDimens

/**
 * Created by A.Elkhami on 22/05/2025.
 */

@Composable
fun ChampionsScreen(
    viewModel: ChampionsViewModel = hiltViewModel(),
    onSeasonClick: (String, String) -> Unit
) {
    LaunchedEffect(true) {
        viewModel.loadChampions()
    }

    ChampionsScreenContent(
        uiState = viewModel.uiState,
        onSeasonClick = onSeasonClick,
        onRefresh = { viewModel.refreshChampions() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChampionsScreenContent(
    uiState: ChampionsUiState,
    onSeasonClick: (String, String) -> Unit,
    onRefresh: () -> Unit
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.error) {
        if (uiState.error != null && uiState.champions.isNotEmpty()) {
            snackbarHostState.showSnackbar(uiState.error.toUiText().asString(context))
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(title = {
                Icon(
                    painter = painterResource(R.drawable.ic_f1),
                    tint = F1Red,
                    contentDescription = null
                )
            })
        }
    ) { innerPadding ->
        val dimens = LocalDimens.current

        val pullToRefreshState = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            state = pullToRefreshState,
            isRefreshing = uiState.isRefreshing,
            onRefresh = onRefresh
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }

                    else -> {
                        LazyColumn(
                            contentPadding = PaddingValues(dimens.medium),
                            verticalArrangement = Arrangement.spacedBy(dimens.verticalSpace),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            if (uiState.champions.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (uiState.error != null) {
                                            Text(
                                                text = uiState.error.toUiText().asString(),
                                                color = MaterialTheme.colorScheme.error,
                                                textAlign = TextAlign.Center
                                            )
                                        } else {
                                            Text(
                                                text = stringResource(R.string.error_unknown),
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            } else {
                                items(uiState.champions, key = { it.season }) { champion ->
                                    ChampionItem(
                                        item = champion,
                                        onClick = {
                                            onSeasonClick(
                                                champion.season,
                                                champion.driver
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}

@Composable
fun ChampionItem(
    item: ChampionItemUiState,
    onClick: () -> Unit
) {
    val dimens = LocalDimens.current
    val seasonLabel = stringResource(id = R.string.label_season)
    val championLabel = stringResource(id = R.string.label_champion)
    val constructorLabel = stringResource(id = R.string.label_constructor)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = dimens.borderWidth,
                color = F1Red,
                shape = RoundedCornerShape(dimens.medium)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(dimens.medium),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.elevation),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(dimens.medium)
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_f1),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(dimens.f1IconSize)
            )

            Column {
                Text(
                    text = "$seasonLabel: ${item.season}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(dimens.small))
                Text(
                    text = "$championLabel: ${item.driver}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "$constructorLabel: ${item.constructor}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChampionsScreenPreview() {
    val previewState = ChampionsUiState(
        isLoading = false,
        champions = listOf(
            ChampionItemUiState(
                title = "2023",
                driver = "Max Verstappen",
                constructor = "Red Bull Racing",
                season = "2023"
            ),
            ChampionItemUiState(
                title = "022",
                driver = "Max Verstappen",
                constructor = "Red Bull Racing",
                season = "2022"
            ),
            ChampionItemUiState(
                title = "2021",
                driver = "Lewis Hamilton",
                constructor = "Mercedes",
                season = "2021"
            )
        )
    )

    ChampionsScreenContent(
        uiState = previewState,
        onSeasonClick = { _, _ -> },
        onRefresh = {}
    )
}

