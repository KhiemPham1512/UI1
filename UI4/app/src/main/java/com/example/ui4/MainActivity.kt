package com.example.ui4

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay

private val LightColors = lightColorScheme(
    primary = Color(0xFF2563EB),
    background = Color(0xFFF8FAFC),
    surface = Color.White,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = Color(0xFF0F172A)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF60A5FA),
    background = Color(0xFF0F172A),
    surface = Color(0xFF1E293B),
    onBackground = Color(0xFFF8FAFC),
    onSurface = Color(0xFFF8FAFC),
    secondaryContainer = Color(0xFF1E3A8A),
    onSecondaryContainer = Color(0xFFE2E8F0)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        // 1. KHỞI TẠO BỘ LƯU TRỮ VĨNH VIỄN TRONG BỘ NHỚ MÁY (SHARED PREFERENCES)
        val sharedPrefs = getSharedPreferences("ui4_theme_prefs", Context.MODE_PRIVATE)

        setContent {
            // 2. ĐỌC TRẠNG THÁI ĐÃ LƯU TỪ LẦN TRƯỚC (Mặc định là false nếu mở app lần đầu)
            var isDarkMode by remember {
                mutableStateOf(sharedPrefs.getBoolean("is_dark_mode", false))
            }

            val currentColorScheme = if (isDarkMode) DarkColors else LightColors

            // Đồng bộ màu thanh trạng thái (Giờ/Pin) và thanh điều hướng
            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as? Activity)?.window
                    if (window != null) {
                        window.statusBarColor = currentColorScheme.background.toArgb()
                        window.navigationBarColor = currentColorScheme.background.toArgb()

                        val insetsController = WindowCompat.getInsetsController(window, view)
                        insetsController.isAppearanceLightStatusBars = !isDarkMode
                        insetsController.isAppearanceLightNavigationBars = !isDarkMode
                    }
                }
            }

            MaterialTheme(colorScheme = currentColorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .systemBarsPadding()
                    ) {
                        UI4AppContainer(
                            isDarkMode = isDarkMode,
                            onDarkModeChange = { newValue ->
                                // 3. CẬP NHẬT GIAO DIỆN NGAY LẬP TỨC
                                isDarkMode = newValue
                                // 4. GHI XUỐNG BỘ NHỚ VẬT LÝ ĐỂ GIỮ NGUYÊN KHI TẮT APP
                                sharedPrefs.edit().putBoolean("is_dark_mode", newValue).apply()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UI4AppContainer(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Welcome) }
    val listState = rememberLazyListState()

    var isListLoading by remember { mutableStateOf(true) }
    LaunchedEffect(currentScreen) {
        if (currentScreen is Screen.ComponentList && isListLoading) {
            delay(1000)
            isListLoading = false
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            val isForward = when {
                initialState is Screen.Welcome && targetState is Screen.ComponentList -> true
                initialState is Screen.ComponentList && targetState is Screen.Detail -> true
                else -> false
            }

            if (isForward) {
                (slideInHorizontally(tween(350)) { fullWidth -> fullWidth } + fadeIn(tween(350)))
                    .togetherWith(slideOutHorizontally(tween(350)) { fullWidth -> -fullWidth / 3 } + fadeOut(tween(350)))
            } else {
                (slideInHorizontally(tween(350)) { fullWidth -> -fullWidth } + fadeIn(tween(350)))
                    .togetherWith(slideOutHorizontally(tween(350)) { fullWidth -> fullWidth / 3 } + fadeOut(tween(350)))
            }
        },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            is Screen.Welcome -> {
                WelcomeScreen(
                    onReadyClick = { currentScreen = Screen.ComponentList }
                )
            }
            is Screen.ComponentList -> {
                BackHandler { currentScreen = Screen.Welcome }
                ComponentListScreen(
                    listState = listState,
                    isLoading = isListLoading,
                    onBackToWelcome = { currentScreen = Screen.Welcome },
                    onItemClick = { item -> currentScreen = Screen.Detail(item) }
                )
            }
            is Screen.Detail -> {
                BackHandler { currentScreen = Screen.ComponentList }
                ComponentDetailScreen(
                    item = screen.item,
                    isDarkMode = isDarkMode,
                    onDarkModeChange = onDarkModeChange,
                    onBackClick = { currentScreen = Screen.ComponentList }
                )
            }
        }
    }
}