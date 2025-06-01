package com.elkhami.f1champions.seasondetails.presentation

import androidx.compose.foundation.border
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun SeasonDetailsScreen(
    season: String,
    championName: String,
    onBackClick: () -> Unit,
    viewModel: SeasonDetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(season) {
        viewModel.loadSeason(season)
    }

    SeasonDetailsScreenContents(
        uiState = viewModel.uiState,
        championName = championName,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeasonDetailsScreenContents(
    uiState: SeasonDetailsUiState,
    championName: String,
    onBackClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(uiState.seasonTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.cd_back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val dimens = LocalDimens.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    Text(
                        text = uiState.error.toUiText().asString(),
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center),
                        textAlign = TextAlign.Center
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding = PaddingValues(dimens.medium),
                        verticalArrangement = Arrangement.spacedBy(dimens.verticalSpace)
                    ) {
                        items(
                            items = uiState.races,
                            key = { it.round } // use unique roundText
                        ) { race ->
                            RaceItem(item = race, championName = championName)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RaceItem(item: RaceItemUiState, championName: String) {
    val dimens = LocalDimens.current

    val roundLabel = stringResource(id = R.string.label_round)
    val winnerLabel = stringResource(id = R.string.label_winner)
    val constructorLabel = stringResource(id = R.string.label_constructor)

    val isChampion = item.winnerName == championName

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = dimens.borderWidth,
                color = if (isChampion) F1Red else Color.Gray,
                shape = RoundedCornerShape(dimens.medium)
            ),
        shape = RoundedCornerShape(dimens.medium),
        elevation = CardDefaults.cardElevation(defaultElevation = dimens.xSmall),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(dimens.small)
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_checkered_flag),
                contentDescription = null,
                tint = if(isChampion) F1Red else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(dimens.flagIconSize)
            )
            Column(modifier = Modifier.padding(dimens.small)) {
                Text(
                    text = "$roundLabel: ${item.round}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(text = item.raceName, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = item.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(dimens.small))
                Text(
                    text = "$winnerLabel: ${item.winnerName}",
                    style = if (isChampion) MaterialTheme.typography.bodyLarge else MaterialTheme.typography.bodyMedium,
                    color = if (isChampion) F1Red else Color.Gray
                )
                Text(
                    text = "$constructorLabel: ${item.constructorName}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun SeasonDetailsScreenPreview() {
    val previewState = SeasonDetailsUiState(
        isLoading = false,
        seasonTitle = "Season 2023",
        races = listOf(
            RaceItemUiState(
                round = "1",
                raceName = "Bahrain Grand Prix",
                date = "March 5, 2023",
                winnerName = "Max Verstappen",
                constructorName = "Red Bull Racing"
            ),
            RaceItemUiState(
                round = "2",
                raceName = "Saudi Arabian Grand Prix",
                date = "March 19, 2023",
                winnerName = "Sergio Pérez",
                constructorName = "Red Bull Racing"
            ),
            RaceItemUiState(
                round = "3",
                raceName = "Australian Grand Prix",
                date = "April 2, 2023",
                winnerName = "Lewis Hamilton",
                constructorName = "Mercedes"
            )
        )
    )

    SeasonDetailsScreenContents(
        uiState = previewState,
        onBackClick = {},
        championName = "Max Verstappen"
    )
}
