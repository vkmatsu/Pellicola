package com.pellicola.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

// Modelo de item na listagem de busca
data class Movie(
    @SerializedName("imdbID") val imdbId: String,
    @SerializedName("Title")  val title: String,
    @SerializedName("Year")   val year: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Type")   val type: String
) : Serializable

// Modelo de detalhes completos do filme
data class MovieDetail(
    @SerializedName("imdbID")   val imdbId: String,
    @SerializedName("Title")    val title: String,
    @SerializedName("Year")     val year: String,
    @SerializedName("Poster")   val poster: String,
    @SerializedName("Plot")     val plot: String,
    @SerializedName("Director") val director: String,
    @SerializedName("Actors")   val actors: String,
    @SerializedName("Genre")    val genre: String,
    @SerializedName("Runtime")  val runtime: String,
    @SerializedName("imdbRating") val rating: String,
    @SerializedName("Language") val language: String,
    @SerializedName("Response") val response: String
) : Serializable

// Resposta da busca
data class SearchResponse(
    @SerializedName("Search")      val movies: List<Movie>?,
    @SerializedName("totalResults") val totalResults: String?,
    @SerializedName("Response")    val response: String,
    @SerializedName("Error")       val error: String?
)
