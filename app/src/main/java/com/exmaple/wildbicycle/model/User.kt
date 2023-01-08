package com.exmaple.wildbicycle.model

import java.util.Date

data class User(
    val id: String = "",
    val email: String = "",
    val provider: ProviderType = ProviderType.EMAIL_PASS,
    val date: Date = Date()
)
