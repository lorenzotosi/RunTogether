package com.app.runtogether

import androidx.compose.ui.graphics.vector.ImageVector

data class MenuItems(
    val id: String,
    val title: String,
    val icon: ImageVector,
    val contentDescription: String,
    val screens: Screens
)