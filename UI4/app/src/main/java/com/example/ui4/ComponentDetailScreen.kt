package com.example.ui4

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentDetailScreen(
    item: UIComponentItem?,
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    val headerTitle = item?.safeTitle ?: "Chi tiết"

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "$headerTitle Detail",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
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
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (item == null) {
                Text("Không tìm thấy dữ liệu thành phần!", color = Color.Red)
            } else {
                when (item.safeId) {
                    "text" -> TextDetailExhibit()
                    "image" -> ImageDetailExhibit()
                    "textfield" -> TextFieldDetailExhibit()
                    "passwordfield" -> PasswordFieldDetailExhibit()
                    "switch" -> SwitchDetailExhibit(isDarkMode, onDarkModeChange)
                    "slider" -> SliderDetailExhibit()
                    "row" -> RowLayoutDetailExhibit()
                    "column" -> ColumnLayoutDetailExhibit()
                    "box" -> BoxLayoutDetailExhibit()
                    "button" -> ButtonDetailExhibit()
                    "checkbox" -> CheckboxDetailExhibit()
                    "radio" -> RadioButtonDetailExhibit()
                    "dropdown" -> ExposedDropdownMenuBoxExhibit()
                    "datepicker" -> DatePickerDetailExhibit()
                    "navigationview" -> NavigationViewDetailExhibit()
                    "tabbar" -> TabBarDetailExhibit()
                    "menu" -> MenuDetailExhibit()
                    "card" -> CardDetailExhibit()
                    "divider" -> DividerDetailExhibit()
                    "dialog" -> DialogDetailExhibit()
                    "skeleton" -> SkeletonDetailExhibit()
                    else -> Text("Đang cập nhật ${item.safeTitle}")
                }
            }
        }
    }
}

// =========================================================
// 1. TEXT DETAIL (ĐÃ PHÓNG TO CỠ CHỮ LÊN 34.SP & 42.SP)
// =========================================================
@Composable
fun TextDetailExhibit() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        val styledText = buildAnnotatedString {
            append("The ")
            withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                append("quick")
            }
            append(" ")
            withStyle(
                style = SpanStyle(
                    color = Color(0xFFB87333),
                    fontWeight = FontWeight.Bold,
                    fontSize = 42.sp // Phóng to chữ Brown nổi bật
                )
            ) {
                append("Brown\n")
            }
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)) {
                append("fox jumps ")
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.ExtraBold)) {
                append("over\n")
            }
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append("the")
            }
            append(" ")
            withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                append("lazy")
            }
            append(" dog.")
        }
        Text(
            text = styledText,
            fontSize = 34.sp, // Tăng từ 26.sp lên 34.sp
            lineHeight = 52.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

// =========================================================
// 2. IMAGE DETAIL (CLICK VÀO LINK SẼ MỞ ẢNH TRÊN MẠNG LUÔN)
// =========================================================
@Composable
fun ImageDetailExhibit() {
    val uthNetworkUrl = "https://xdcs.cdnchinhphu.vn/446259493575335936/2024/8/17/gt1-1720610920454798297957-17239112310021499437011.jpg"
    val uriHandler = LocalUriHandler.current

    var networkBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoadingNetwork by remember { mutableStateOf(true) }

    LaunchedEffect(uthNetworkUrl) {
        isLoadingNetwork = true
        networkBitmap = withContext(Dispatchers.IO) {
            try {
                val stream = URL(uthNetworkUrl).openStream()
                BitmapFactory.decodeStream(stream)?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        }
        isLoadingNetwork = false
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // ẢNH TRÊN MẠNG (Có thể click vào ảnh hoặc link để mở trình duyệt)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp)
                .clickable { uriHandler.openUri(uthNetworkUrl) },
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                when {
                    isLoadingNetwork -> {
                        Box(modifier = Modifier.fillMaxSize().background(shimmerBrush()))
                    }
                    networkBitmap != null -> {
                        Image(
                            bitmap = networkBitmap!!,
                            contentDescription = "UTH Network Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.img_uth_local),
                            contentDescription = "Fallback Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        // ĐƯỜNG LINK CLICK VÀO MỞ TRỰC TIẾP ẢNH TRÊN MẠNG
        Text(
            text = uthNetworkUrl,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable {
                    // Mở trình duyệt web đến thẳng đường link ảnh khi click
                    uriHandler.openUri(uthNetworkUrl)
                }
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // ẢNH TRONG MÁY (LOCAL DRAWABLE)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(195.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_uth_local),
                contentDescription = "Local UTH Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = "In app",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
        )
    }
}

// =========================================================
// 3. TEXTFIELD DETAIL
// =========================================================
@Composable
fun TextFieldDetailExhibit() {
    var textInput by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Trình mô phỏng TextField", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = textInput,
                onValueChange = { textInput = it },
                placeholder = { Text("Thông tin nhập:") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = textInput.ifEmpty { "Tự động cập nhật dữ liệu theo textfield" },
                color = Color(0xFFD32F2F),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
    }
}

// =========================================================
// 4. PASSWORDFIELD DETAIL
// =========================================================
@Composable
fun PasswordFieldDetailExhibit() {
    var password by remember { mutableStateOf("") }
    var isVisible by remember { mutableStateOf(false) }

    val strengthText = when {
        password.isEmpty() -> "Chưa nhập mật khẩu"
        password.length < 6 -> "Mật khẩu yếu (Cần >= 6 ký tự)"
        password.length < 10 -> "Mật khẩu khá"
        else -> "Mật khẩu rất mạnh!"
    }
    val strengthColor = when {
        password.isEmpty() -> Color.Gray
        password.length < 6 -> Color(0xFFDC2626)
        password.length < 10 -> Color(0xFFD97706)
        else -> Color(0xFF16A34A)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Bảo mật PasswordField", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Nhập mật khẩu") },
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { isVisible = !isVisible }) {
                        Icon(
                            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Toggle password"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = strengthText, color = strengthColor, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = "${password.length} ký tự", color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}

// =========================================================
// 5. SWITCH DETAIL
// =========================================================
@Composable
fun SwitchDetailExhibit(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                contentDescription = null,
                tint = if (isDarkMode) Color(0xFFFBBF24) else Color(0xFFF59E0B),
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isDarkMode) "Giao diện Tối (Dark Mode)" else "Giao diện Sáng (Light Mode)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Gạt công tắc bên dưới để đổi màu toàn bộ ứng dụng ngay lập tức",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Switch(
                checked = isDarkMode,
                onCheckedChange = onDarkModeChange
            )
        }
    }
}

// =========================================================
// 6. SLIDER DETAIL
// =========================================================
@Composable
fun SliderDetailExhibit() {
    var sliderValue by remember { mutableFloatStateOf(65f) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = when {
                    sliderValue == 0f -> Icons.Default.VolumeOff
                    sliderValue < 50f -> Icons.Default.VolumeDown
                    else -> Icons.Default.VolumeUp
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size((36 + (sliderValue * 0.3f)).dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Âm lượng: ${sliderValue.toInt()}%",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(20.dp))

            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 0f..100f,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf(0f, 50f, 100f).forEach { preset ->
                    OutlinedButton(onClick = { sliderValue = preset }) {
                        Text("${preset.toInt()}%")
                    }
                }
            }
        }
    }
}

// =========================================================
// 7, 8, 9. ROW, COLUMN, BOX LAYOUT
// =========================================================
@Composable
fun RowLayoutDetailExhibit() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Row Layout (Xếp ngang X-Axis)", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            repeat(4) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(modifier = Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF93C5FD)))
                    Box(modifier = Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF2563EB)))
                    Box(modifier = Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF93C5FD)))
                }
            }
        }
    }
}

@Composable
fun ColumnLayoutDetailExhibit() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Column Layout (Xếp dọc Y-Axis)", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            val items = listOf(
                Triple("Tầng 1 (Top)", Color(0xFF60A5FA), "Phần tử đầu tiên"),
                Triple("Tầng 2 (Middle)", Color(0xFF3B82F6), "Phần tử ở giữa"),
                Triple("Tầng 3 (Bottom)", Color(0xFF1D4ED8), "Phần tử dưới cùng")
            )
            items.forEach { (title, bgColor, sub) ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(sub, color = Color.White.copy(alpha = 0.85f), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun BoxLayoutDetailExhibit() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Box Layout (Xếp chồng Z-Axis)", fontWeight = FontWeight.Bold, fontSize = 17.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .size(250.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFDBEAFE))
                    .padding(12.dp)
            ) {
                Text("Lớp 1 (Nền)", color = Color(0xFF1E3A8A), fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopStart))

                Box(
                    modifier = Modifier
                        .size(170.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF60A5FA))
                        .padding(10.dp)
                ) {
                    Text("Lớp 2", color = Color.White, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.TopStart))

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E3A8A)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Lớp 3\n(Đỉnh)", color = Color.White, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// =========================================================
// 10. BUTTON DETAIL
// =========================================================
@Composable
fun ButtonDetailExhibit() {
    var count by remember { mutableIntStateOf(0) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "$count",
                fontSize = 48.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text("Số lần tương tác nút bấm", fontSize = 13.sp, color = Color.Gray)

            Button(
                onClick = { count++ },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Filled Button: Tăng +1", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            ElevatedButton(
                onClick = { count += 5 },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Elevated Button: Tăng +5", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = { count = 0 },
                modifier = Modifier.fillMaxWidth().height(54.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("Outlined Button: Đặt lại về 0", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// =========================================================
// 11. CHECKBOX DETAIL
// =========================================================
@Composable
fun CheckboxDetailExhibit() {
    val tasks = remember {
        mutableStateListOf(
            "Học kiến trúc Jetpack Compose" to true,
            "Thực hành State & Recomposition" to true,
            "Tích hợp hiệu ứng Skeleton Loading" to false,
            "Đẩy mã nguồn sạch lên GitHub" to false
        )
    }
    val completedCount = tasks.count { it.second }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tiến độ học tập", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("$completedCount/${tasks.size} hoàn thành", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { completedCount / tasks.size.toFloat() },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            tasks.forEachIndexed { index, (title, isChecked) ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { tasks[index] = title to !isChecked },
                    color = if (isChecked) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { tasks[index] = title to it }
                        )
                        Text(
                            text = title,
                            fontSize = 15.sp,
                            fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
// 12. RADIOBUTTON DETAIL
// =========================================================
@Composable
fun RadioButtonDetailExhibit() {
    val options = listOf(
        "Sinh viên năm 1" to "Nhập môn lập trình & Cơ sở dữ liệu",
        "Sinh viên năm 2" to "Cấu trúc dữ liệu & Mạng máy tính",
        "Sinh viên năm 3" to "Lập trình Thiết bị Di động & AI",
        "Sinh viên năm cuối" to "Thực tập doanh nghiệp & Khóa luận"
    )
    var selected by remember { mutableStateOf(options[2].first) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Chọn cấp bậc sinh viên", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))

            options.forEach { (title, subtitle) ->
                val isSelected = (selected == title)
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selected = title },
                    color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f) else Color.Transparent
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        RadioButton(selected = isSelected, onClick = { selected = title })
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text(text = subtitle, fontSize = 12.sp, color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

// =========================================================
// 13. EXPOSEDDROPDOWNMENUBOX DETAIL (ĐÚNG TÊN KOTLIN & ĐÚNG ĐỊA CHỈ 3 CƠ SỞ UTH)
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuBoxExhibit() {
    var expanded by remember { mutableStateOf(false) }

    // CẬP NHẬT CHÍNH XÁC ĐỊA CHỈ 3 CƠ SỞ ĐÀO TẠO UTH THEO ẢNH CUNG CẤP
    val campuses = listOf(
        "CƠ SỞ 1" to "Số 02 đường Võ Oanh, phường Thạnh Mỹ Tây, TP.HCM",
        "CƠ SỞ 2" to "Số 10 đường số 12, phường An Khánh, TP. HCM",
        "CƠ SỞ 3" to "Số 70, đường Tô Ký, phường Trung Mỹ Tây, TP. HCM"
    )
    var selectedCampus by remember { mutableStateOf(campuses[0]) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ExposedDropdownMenuBox",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Chọn Cơ Sở Đào Tạo UTH",
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedCampus.first,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Cơ sở đào tạo") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    campuses.forEach { campus ->
                        DropdownMenuItem(
                            text = {
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(campus.first, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                    Text(campus.second, fontSize = 12.sp, color = Color.Gray)
                                }
                            },
                            onClick = {
                                selectedCampus = campus
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = selectedCampus.first,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = selectedCampus.second,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
            }
        }
    }
}

// =========================================================
// 14, 15, 16. NHÓM NAVIGATION: TOPAPPBAR, TABROW, DROPDOWNMENU
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationViewDetailExhibit() {
    var lastAction by remember { mutableStateOf("Chưa bấm nút nào trên TopAppBar") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mô phỏng TopAppBar", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                TopAppBar(
                    title = { Text("Trang Chủ UTH", fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { lastAction = "Đã bấm nút Menu (Hamburger)" }) {
                            Icon(Icons.Default.Menu, contentDescription = null)
                        }
                    },
                    actions = {
                        IconButton(onClick = { lastAction = "Đã bấm nút Tìm kiếm (Search)" }) {
                            Icon(Icons.Default.Search, contentDescription = null)
                        }
                        IconButton(onClick = { lastAction = "Đã bấm nút Thông báo (Notifications)" }) {
                            Icon(Icons.Default.Notifications, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = lastAction, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun TabBarDetailExhibit() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Tổng quan", "Lịch học", "Điểm số")

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Mô phỏng TabRow", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = { Text(title, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Đang hiển thị nội dung tab: ${tabs[selectedTab]}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun MenuDetailExhibit() {
    var expanded by remember { mutableStateOf(false) }
    var selectedAction by remember { mutableStateOf("Chưa chọn thao tác nào") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Popup DropdownMenu", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Box {
                Button(
                    onClick = { expanded = true },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Bấm để mở Menu Tùy Chọn")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Chỉnh sửa hồ sơ") },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) },
                        onClick = { selectedAction = "Đã chọn: Chỉnh sửa hồ sơ"; expanded = false }
                    )
                    DropdownMenuItem(
                        text = { Text("Chia sẻ ứng dụng") },
                        leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) },
                        onClick = { selectedAction = "Đã chọn: Chia sẻ ứng dụng"; expanded = false }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(
                        text = { Text("Đăng xuất", color = Color(0xFFDC2626)) },
                        leadingIcon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = Color(0xFFDC2626)) },
                        onClick = { selectedAction = "Đã chọn: Đăng xuất"; expanded = false }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(text = selectedAction, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        }
    }
}

// =========================================================
// 17, 18, 19, 20, 21. DATEPICKER, CARD, DIVIDER, DIALOG, SKELETON
// =========================================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDetailExhibit() {
    val datePickerState = rememberDatePickerState()
    Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        DatePicker(state = datePickerState, showModeToggle = false)
    }
}

@Composable
fun CardDetailExhibit() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Thẻ Card Chứa Dữ Liệu", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Card giúp đóng gói nội dung và tạo chiều sâu không gian 3D nhờ bóng đổ.", color = Color.Gray)
        }
    }
}

@Composable
fun DividerDetailExhibit() {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Khu vực nội dung phía trên", fontWeight = FontWeight.SemiBold)
            HorizontalDivider(thickness = 2.dp, color = MaterialTheme.colorScheme.primary)
            Text("Khu vực nội dung phía dưới được tách biệt bằng HorizontalDivider", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun DialogDetailExhibit() {
    var showDialog by remember { mutableStateOf(false) }
    Button(
        onClick = { showDialog = true },
        modifier = Modifier.fillMaxWidth().height(54.dp),
        shape = RoundedCornerShape(14.dp)
    ) {
        Text("Nhấn để mở Hộp Thoại (AlertDialog)", fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Xác nhận hành động") },
            text = { Text("Đây là hộp thoại cảnh báo người dùng trước khi thực hiện thao tác quan trọng.") },
            confirmButton = { TextButton(onClick = { showDialog = false }) { Text("Đồng ý") } },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Hủy bỏ") } }
        )
    }
}

@Composable
fun SkeletonDetailExhibit() {
    var isLoading by remember { mutableStateOf(true) }
    val brush = shimmerBrush(showShimmer = isLoading)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { isLoading = !isLoading },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isLoading) "Bấm để xem: Dữ liệu đã tải xong" else "Bấm để xem: Hiệu ứng Skeleton")
        }
        Spacer(modifier = Modifier.height(24.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(if (isLoading) brush else Brush.linearGradient(listOf(Color(0xFF2F80ED), Color(0xFF2F80ED))))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth(0.7f).height(18.dp).clip(RoundedCornerShape(4.dp)).background(brush))
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(modifier = Modifier.fillMaxWidth(0.4f).height(14.dp).clip(RoundedCornerShape(4.dp)).background(brush))
                    } else {
                        Text(text = "Phạm Gia Khiêm", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Text(text = "Sinh viên CNTT - UTH", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}