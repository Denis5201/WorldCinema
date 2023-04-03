package com.example.worldcinema.domain.repository

import com.example.worldcinema.domain.model.*
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getCover(): Flow<Result<Cover>>

    fun getMovies(filterMain: FilterMain): Flow<Result<List<Movie>>>

    fun getEpisodes(movieId: String): Flow<Result<List<Episode>>>

    fun getHistory(): Flow<Result<List<EpisodeHistory>>>

    fun getMovieById(movieId: String): Flow<Result<Movie>>
}