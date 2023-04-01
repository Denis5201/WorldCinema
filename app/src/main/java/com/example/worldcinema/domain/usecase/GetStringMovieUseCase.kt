package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Movie
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class GetStringMovieUseCase @Inject constructor() {

    operator fun invoke(movie: Movie): String = Json.encodeToString(movie)
}