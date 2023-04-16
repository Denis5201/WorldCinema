package com.example.worldcinema

import java.time.format.DateTimeFormatter

object Constants {
    const val FAVOUR_COLLECTION = "Избранное"
    const val BASE_URL = "http://107684.web.hosting-russia.ru:8000/api/"
    const val AUTHORIZATION_HEADER = "Authorization"
    const val ICON_COLLECTION_REQUEST = "iconRequest"
    const val ICON_PARAMETER = "iconParameter"

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
}