/*
 * Copyright (C) 2025 AKS-Labs
 *
 * Ported from CircleToSearch (AKS-Labs): lets the user choose between the
 * multi-engine results sheet and Google Lens for a circled area.
 */

package com.fuzzyfly0.searchandtranslate.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.ManageSearch
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fuzzyfly0.searchandtranslate.R
import com.fuzzyfly0.searchandtranslate.utils.UIPreferences

/**
 * Search method selector: Multi-Search (the Google / Bing / TinEye results
 * sheet) or Google Lens (hand the selection straight to Lens).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchMethodSelector(
    isLensOnly: Boolean,
    onMethodChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.section_search_method),
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            ),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                onClick = { onMethodChange(false) },
                selected = !isLensOnly,
                icon = { SegmentedButtonDefaults.Icon(!isLensOnly) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ManageSearch,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.search_method_multi), style = MaterialTheme.typography.labelLarge)
                }
            }
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                onClick = { onMethodChange(true) },
                selected = isLensOnly,
                icon = { SegmentedButtonDefaults.Icon(isLensOnly) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.search_method_lens), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
        if (isLensOnly) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.info_lens_needs_google),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * State-holding wrapper around [SearchMethodSelector]: reads the preference
 * once and writes it back through [UIPreferences.setUseGoogleLensOnly], which
 * commits synchronously so the overlay sees the new value before it searches.
 */
@Composable
fun UnifiedSearchMethodSelector(
    uiPreferences: UIPreferences,
    modifier: Modifier = Modifier
) {
    var isLensOnly by remember { mutableStateOf(uiPreferences.isUseGoogleLensOnly()) }

    SearchMethodSelector(
        isLensOnly = isLensOnly,
        onMethodChange = { newValue ->
            isLensOnly = newValue
            uiPreferences.setUseGoogleLensOnly(newValue)
        },
        modifier = modifier
    )
}
