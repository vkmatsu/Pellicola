package com.pellicola.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.pellicola.adapters.FavoritesAdapter
import com.pellicola.databinding.ActivityFavoritesBinding
import com.pellicola.models.MovieDetail
import com.pellicola.storage.FavoritesManager
import androidx.recyclerview.widget.LinearLayoutManager

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding
    private lateinit var adapter: FavoritesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        // Recarrega ao voltar da tela de detalhes (caso algum favorito seja removido)
        loadFavorites()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Quero assistir"
    }

    private fun setupRecyclerView() {
        adapter = FavoritesAdapter(
            mutableListOf(),
            onMovieClick = { movie -> openDetail(movie) },
            onRemoveClick = { movie -> removeFavorite(movie) }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun loadFavorites() {
        val favorites = FavoritesManager.getFavorites(this)
        adapter.updateMovies(favorites)
        showEmpty(favorites.isEmpty())

        val count = favorites.size
        binding.tvCount.text = if (count == 1) "1 filme salvo" else "$count filmes salvos"
    }

    private fun openDetail(movie: MovieDetail) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_IMDB_ID, movie.imdbId)
            putExtra(DetailActivity.EXTRA_TITLE, movie.title)
        }
        startActivity(intent)
    }

    private fun removeFavorite(movie: MovieDetail) {
        FavoritesManager.removeFavorite(this, movie.imdbId)
        adapter.removeItem(movie)
        val remaining = FavoritesManager.getFavorites(this)
        showEmpty(remaining.isEmpty())
        val count = remaining.size
        binding.tvCount.text = if (count == 1) "1 filme salvo" else "$count filmes salvos"
    }

    private fun showEmpty(empty: Boolean) {
        binding.tvEmpty.visibility      = if (empty) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (empty) View.GONE    else View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
