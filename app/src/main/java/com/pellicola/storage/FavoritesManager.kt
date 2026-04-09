package com.pellicola.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pellicola.models.MovieDetail

object FavoritesManager {

    private const val PREFS_NAME   = "pellicola_prefs"
    private const val KEY_FAVORITES = "favorites"

    private val gson = Gson()

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // Retorna todos os favoritos salvos
    fun getFavorites(context: Context): MutableList<MovieDetail> {
        val json = getPrefs(context).getString(KEY_FAVORITES, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<MovieDetail>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    // Salva um filme nos favoritos
    fun addFavorite(context: Context, movie: MovieDetail) {
        val favorites = getFavorites(context)
        if (favorites.none { it.imdbId == movie.imdbId }) {
            favorites.add(movie)
            saveAll(context, favorites)
        }
    }

    // Remove um filme dos favoritos
    fun removeFavorite(context: Context, imdbId: String) {
        val favorites = getFavorites(context)
        favorites.removeAll { it.imdbId == imdbId }
        saveAll(context, favorites)
    }

    // Verifica se um filme já é favorito
    fun isFavorite(context: Context, imdbId: String): Boolean {
        return getFavorites(context).any { it.imdbId == imdbId }
    }

    private fun saveAll(context: Context, favorites: List<MovieDetail>) {
        getPrefs(context).edit()
            .putString(KEY_FAVORITES, gson.toJson(favorites))
            .apply()
    }
}
