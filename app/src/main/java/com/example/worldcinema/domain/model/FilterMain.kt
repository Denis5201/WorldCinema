package com.example.worldcinema.domain.model

enum class FilterMain(val inRequest: String) {
    NEW("new"),
    IN_TREND("inTrend"),
    FOR_ME("forMe"),
    LAST_VIEW("lastView"),
    COMPILATION("compilation")
}