package com.pellicola.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.pellicola.adapters.MovieAdapter
import com.pellicola.databinding.ActivitySearchResultsBinding
import com.pellicola.models.Movie
import com.pellicola.network.OmdbClient
import kotlinx.coroutines.launch

class SearchResultsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_QUERY = "extra_query"
    }

    private lateinit var binding: ActivitySearchResultsBinding
    private lateinit var adapter: MovieAdapter
    private var query: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        query = intent.getStringExtra(EXTRA_QUERY) ?: ""

        setupToolbar()
        setupRecyclerView()
        searchMovies(query)

        // Nova busca a partir desta tela
        binding.btnSearch.setOnClickListener {
            val newQuery = binding.etSearch.text.toString().trim()
            if (newQuery.isNotEmpty()) {
                searchMovies(newQuery)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.etSearch.setText(query)
    }

    private fun setupRecyclerView() {
        adapter = MovieAdapter(emptyList()) { movie ->
            openDetail(movie)
        }
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
        binding.recyclerView.adapter = adapter
    }

    private fun searchMovies(searchQuery: String) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = OmdbClient.api.searchMovies(searchQuery)
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body?.response == "True" && !body.movies.isNullOrEmpty()) {
                        val movies = body.movies
                        binding.tvResultCount.text =
                            "${body.totalResults ?: movies.size} resultados para \"$searchQuery\""
                        adapter.updateMovies(movies)
                        showEmpty(false)
                    } else {
                        showEmpty(true, body?.error ?: "Nenhum filme encontrado.")
                    }
                } else {
                    showEmpty(true, "Erro na busca. Tente novamente.")
                }
            } catch (e: Exception) {
                showEmpty(true, "Sem conexão com a internet.")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun openDetail(movie: Movie) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(DetailActivity.EXTRA_IMDB_ID, movie.imdbId)
            putExtra(DetailActivity.EXTRA_TITLE, movie.title)
        }
        startActivity(intent)
    }

    private fun showLoading(loading: Boolean) {
        binding.progressBar.visibility  = if (loading) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (loading) View.GONE   else View.VISIBLE
    }

    private fun showEmpty(empty: Boolean, message: String = "") {
        binding.tvEmpty.visibility      = if (empty) View.VISIBLE else View.GONE
        binding.recyclerView.visibility = if (empty) View.GONE    else View.VISIBLE
        if (empty) binding.tvEmpty.text = message
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
