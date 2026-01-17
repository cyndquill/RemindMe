package com.cyndquil.remindme.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun NavigationPanel() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Remind Me", modifier = Modifier.padding(16.dp))
                HorizontalDivider()
                NavigationDrawerItem(
                    label = { Text(text = "Landing Page") },
                    selected = true,
                    onClick = { /* TODO */ }
                )
            }
        },
    ) {
        Scaffold(
            topBar = { TopBar(scope, drawerState) }
        ) { innerPadding ->
            PlaceHolderText(
                header = "Remind Me",
                footer = "by cyndquil",
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
