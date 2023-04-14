package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Episode
import javax.inject.Inject

class CalculateReleaseYearsUseCase @Inject constructor() {

    operator fun invoke(episodes: List<Episode>): String {
        if (episodes.isEmpty()) {
            return "-"
        }
        val minYear = episodes.minOf { it.year }
        val maxYear = episodes.maxOf { it.year }
        return if (minYear == maxYear) "$maxYear" else "$minYear - $maxYear"
    }
}