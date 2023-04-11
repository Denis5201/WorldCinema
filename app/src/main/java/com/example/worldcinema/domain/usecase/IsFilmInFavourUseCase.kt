package com.example.worldcinema.domain.usecase

import com.example.worldcinema.domain.model.Movie
import javax.inject.Inject

class IsFilmInFavourUseCase @Inject constructor() {

    operator fun invoke(movieId: String, favourFilms: List<Movie>): Boolean {
        val ourFilm = favourFilms.find { movieId == it.movieId }
        return ourFilm != null
    }
}