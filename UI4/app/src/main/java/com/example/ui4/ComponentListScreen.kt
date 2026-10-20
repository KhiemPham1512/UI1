package com.example.ui4

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentListScreen(
    listState: LazyListState,
    isLoading: Boolean,
    onBackToWelcome: () -> Unit,
    onItemClick: (UIComponentItem) -> Unit
) {
    // Danh sách đầy đủ các nhóm, bao gồm cả NavigationView, TabBar và Menu
    val rawBackendData: List<UIComponentItem?> = remember {
        listOf(
            // Display
            UIComponentItem("text", "Text", "Displays text with styling", "Display"),
            UIComponentItem("image", "Image", "Displays an image from URL & local", "Display"),
            // Input
            UIComponentItem("textfield", "TextField", "Input field for text", "Input"),
            UIComponentItem("passwordfield", "PasswordField", "Input field for passwords", "Input"),
            UIComponentItem("switch", "Switch", "Enables switching between two opposite states", "Input"),
            UIComponentItem("slider", "Slider", "Selects a value from a range", "Input"),
            UIComponentItem("datepicker", "DatePicker", "Selects a date and time", "Input"),
            // Layout
            UIComponentItem("column", "Column", "Arranges elements vertically", "Layout"),
            UIComponentItem("row", "Row Layout", "Arranges elements horizontally", "Layout"),
            UIComponentItem("box", "Box", "Stacks elements on top of each other", "Layout"),
            // Selection
            UIComponentItem("button", "Button", "Triggers an action when tapped", "Selection"),
            UIComponentItem("checkbox", "Checkbox", "Selects one or multiple options", "Selection"),
            UIComponentItem("radio", "RadioButton", "Selects a single option", "Selection"),
            UIComponentItem("dropdown", "ExposedDropdownMenuBox", "Selects a value from a dropdown list", "Selection"),
            // Navigation (Đã khôi phục đầy đủ theo Slide 64)
            UIComponentItem("navigationview", "NavigationView (TopBar)", "Header with title and back button", "Navigation"),
            UIComponentItem("tabbar", "TabBar (TabRow)", "Switches between main sections", "Navigation"),
            UIComponentItem("menu", "Menu", "Shows a popup menu with multiple actions", "Navigation"),
            // Content
            UIComponentItem("card", "Card", "Container with content and style", "Content"),
            UIComponentItem("divider", "Divider", "Separates content into sections", "Content"),
            UIComponentItem("dialog", "AlertDialog", "Popup message requiring user action", "Content"),
            UIComponentItem("skeleton", "Skeleton Effect", "Shimmer loading animation demo", "Content")
        )
    }

    val validItems = remember(rawBackendData) { rawBackendData.filterNotNull() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "UI Components List",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackToWelcome) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back to Welcome",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoading) {
                SkeletonLoadingListView()
            } else {
                val grouped = validItems.groupBy { it.safeCategory }

                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    grouped.forEach { (category, items) ->
                        item {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = category,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                        }

                        items(items, key = { it.safeId }) { item ->
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { onItemClick(item) },
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)) {
                                    Text(
                                        text = item.safeTitle,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = item.safeDescription,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}