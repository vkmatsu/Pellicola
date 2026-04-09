package com.pellicola.activities

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.pellicola.R
import com.pellicola.databinding.ActivityDetailBinding
import com.pellicola.models.MovieDetail
import com.pellicola.network.OmdbClient
import com.pellicola.storage.FavoritesManager
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_IMDB_ID = "extra_imdb_id"
        const val EXTRA_TITLE   = "extra_title"
    }

    private lateinit var binding: ActivityDetailBinding
    private var movieDetail: MovieDetail? = null
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imdbId = intent.getStringExtra(EXTRA_IMDB_ID) ?: return
        val title  = intent.getStringExtra(EXTRA_TITLE) ?: "Detalhes"

        setupToolbar(title)
        loadMovieDetail(imdbId)

        binding.fabFavorite.setOnClickListener {
            toggleFavorite()
        }
    }

    private fun setupToolbar(title: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = title
    }

    private fun loadMovieDetail(imdbId: String) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = OmdbClient.api.getMovieDetail(imdbId)
                if (response.isSuccessful && response.body()?.response == "True") {
                    movieDetail = response.body()
                    movieDetail?.let {
                        displayMovie(it)
                        isFavorite = FavoritesManager.isFavorite(this@DetailActivity, it.imdbId)
                        updateFabIcon()
                    }
                } else {
                    Toast.makeText(this@DetailActivity,
                        "Não foi possível carregar os detalhes.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailActivity,
                    "Sem conexão com a internet.", Toast.LENGTH_SHORT).show()
                finish()
            } finally {
                showLoading(false)
            }
        }
    }

    private fun displayMovie(movie: MovieDetail) {
        binding.tvTitle.text      = movie.title
        binding.tvYear.text       = movie.year
        binding.tvGenre.text      = movie.genre
        binding.tvRuntime.text    = movie.runtime
        binding.tvRating.text     = "⭐ ${movie.rating}"
        binding.tvDirector.text   = "Diretor: ${movie.director}"
        binding.tvActors.text     = "Elenco: ${movie.actors}"
        binding.tvLanguage.text   = "Idioma: ${movie.language}"
        binding.tvPlot.text       = movie.plot

        if (movie.poster != "N/A" && movie.poster.isNotEmpty()) {
            Picasso.get()
                .load(movie.poster)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_movie_placeholder)
                .into(binding.imgPoster)
        } else {
            binding.imgPoster.setImageResource(R.drawable.ic_movie_placeholder)
        }
    }

    private fun toggleFavorite() {
        val movie = movieDetail ?: return
        if (isFavorite) {
            FavoritesManager.removeFavorite(this, movie.imdbId)
            isFavorite = false
            Toast.makeText(this, "Removido dos favoritos", Toast.LENGTH_SHORT).show()
        } else {
            FavoritesManager.addFavorite(this, movie)
            isFavorite = true
            Toast.makeText(this, "Adicionado aos favoritos!", Toast.LENGTH_SHORT).show()
        }
        updateFabIcon()
    }

    private fun updateFabIcon() {
        val icon = if (isFavorite) R.drawable.ic_favorite_filled
                   else            R.drawable.ic_favorite_border
        binding.fabFavorite.setImageResource(icon)
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.scrollContent.visibility = if (loading) View.GONE else View.VISIBLE
        binding.fabFavorite.visibility = if (loading) View.GONE else View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
