package com.example.ui4

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(onReadyClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // CỤM NỘI DUNG CHÍNH Ở GIỮA MÀN HÌNH (Dùng weight 1f để tự căn giữa hoàn hảo)
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. LOGO JETPACK COMPOSE TO BẢN (190.dp) SẮC NÉT
            Image(
                painter = painterResource(id = R.drawable.ic_compose_logo),
                contentDescription = "Jetpack Compose Logo",
                modifier = Modifier
                    .size(190.dp)
                    .padding(8.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 2. HUY HIỆU CÔNG NGHỆ NHỎ ĐẲNG CẤP
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "ANDROID MODERN TOOLKIT",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 3. TIÊU ĐỀ JETPACK COMPOSE TO RÕ RỆT (32.sp ExtraBold)
            Text(
                text = "Jetpack Compose",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.5).sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. ĐOẠN VĂN BẢN MÔ TẢ ĐƯỢC PHÓNG TO, NÉT CHỮ DỄ ĐỌC (16.sp)
            Text(
                text = "Jetpack Compose is a modern UI toolkit for building native Android applications using a declarative programming approach.",
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f),
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 10.dp),
                textAlign = TextAlign.Center
            )
        }

        // 5. NÚT "I'M READY" NỔI BẬT, DÀY DẶN, NHẤC CAO CÁCH ĐÁY
        Button(
            onClick = onReadyClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp), // Chiều cao 58.dp đầm tay, bắt mắt
            shape = RoundedCornerShape(29.dp), // Bo tròn dáng viên thuốc
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 6.dp, // Đổ bóng nổi bật tách khỏi nền
                pressedElevation = 2.dp
            )
        ) {
            Text(
                text = "I'm ready  ➔",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        // Khoảng đệm nâng nút lên cao hơn vạch cử chỉ đáy
        Spacer(modifier = Modifier.height(28.dp))
    }
}