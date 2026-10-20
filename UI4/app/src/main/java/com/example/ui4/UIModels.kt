package com.example.ui4

// Model phòng thủ: Cho phép nhận null từ DB/Backend nhưng tự động làm sạch (Sanitize) dữ liệu
data class UIComponentItem(
    val id: String?,
    val title: String?,
    val description: String?,
    val category: String?
) {
    // Thuộc tính an toàn (Safe Getters) - Đảm bảo tầng UI không bao giờ bị crash
    val safeId: String
        get() = id?.takeIf { it.isNotBlank() } ?: "unknown_component"

    val safeTitle: String
        get() = title?.takeIf { it.isNotBlank() } ?: "Thành phần chưa đặt tên"

    val safeDescription: String
        get() = description?.takeIf { it.isNotBlank() } ?: "Chưa có thông tin mô tả chi tiết."

    val safeCategory: String
        get() = category?.takeIf { it.isNotBlank() } ?: "Khác"
}

// Sealed class điều hướng an toàn: Cho phép Screen.Detail nhận item nullable
sealed class Screen {
    object Welcome : Screen()
    object ComponentList : Screen()
    data class Detail(val item: UIComponentItem?) : Screen()
}