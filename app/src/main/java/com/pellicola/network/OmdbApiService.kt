package com.pellicola.network

import com.pellicola.models.MovieDetail
import com.pellicola.models.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OmdbApiService {

    // Busca filmes por título
    @GET("/")
    suspend fun searchMovies(
        @Query("s")      query: String,
        @Query("apikey") apiKey: String = OmdbClient.API_KEY,
        @Query("type")   type: String = "movie"
    ): Response<SearchResponse>

    // Busca detalhes de um filme pelo ID
    @GET("/")
    suspend fun getMovieDetail(
        @Query("i")      imdbId: String,
        @Query("apikey") apiKey: String = OmdbClient.API_KEY,
        @Query("plot")   plot: String = "full"
    ): Response<MovieDetail>
}
