package com.example.worldcinema.data.repository

import android.util.Log
import com.example.worldcinema.data.api.MovieApi
import com.example.worldcinema.domain.model.*
import com.example.worldcinema.domain.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApi
) : MovieRepository{

    override fun getCover(): Flow<Result<Cover>> = flow {
        try {
            val cover = api.getCover().toCover()
            emit(Result.success(cover))
        } catch (e: Exception) {
            Log.e("OPS getCover", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getMovies(filterMain: FilterMain): Flow<Result<List<Movie>>> = flow {
        try {
            val movieList = api.getListMovie(filterMain.inRequest).map { it.toMovie() }
            emit(Result.success(movieList))
        } catch (e: Exception) {
            Log.e("OPS getMovies", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getEpisodes(movieId: String): Flow<Result<List<Episode>>> = flow {
        try {
            val episodeList = api.getListEpisode(movieId).map { it.toEpisode() }
            emit(Result.success(episodeList))
        } catch (e: Exception) {
            Log.e("OPS getEpisodes", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getHistory(): Flow<Result<List<EpisodeHistory>>> = flow {
        try {
            val episodeHistoryList = api.getHistory().map { it.toEpisodeHistory() }
            emit(Result.success(episodeHistoryList))
        } catch (e: Exception) {
            Log.e("OPS getLastMoviesWithHistory", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun getMovieById(movieId: String): Flow<Result<Movie>> = flow {
        try {
            val movie = api.getListMovie(FilterMain.LAST_VIEW.inRequest)
                .filter { it.movieId == movieId }
                .map { it.toMovie() }
            if (movie.isEmpty()) {
                throw Exception("movie not found")
            }
            emit(Result.success(movie.first()))
        } catch (e: Exception) {
            Log.e("OPS getMovieById", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun dislike(movieId: String): Flow<Result<Unit>> = flow {
        try {
            api.dislike(movieId)
            emit(Result.success(Unit))
        } catch (e: Exception) {
            Log.e("OPS dislike", e.message.toString())
            emit(Result.failure(e))
        }
    }.flowOn(Dispatchers.IO)
}