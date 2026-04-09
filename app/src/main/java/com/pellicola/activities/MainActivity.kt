package com.pellicola.activities

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pellicola.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        // Botão de busca
        binding.btnSearch.setOnClickListener {
            performSearch()
        }

        // Tecla Enter no teclado
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch()
                true
            } else false
        }

        // Botão de favoritos no canto superior direito
        binding.btnFavorites.setOnClickListener {
            startActivity(Intent(this, FavoritesActivity::class.java))
        }
    }

    private fun performSearch() {
        val query = binding.etSearch.text.toString().trim()
        if (query.isEmpty()) {
            Toast.makeText(this, "Digite o nome de um filme", Toast.LENGTH_SHORT).show()
            return
        }

        // Esconde o teclado
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)

        // Navega para a tela de resultados passando o query
        val intent = Intent(this, SearchResultsActivity::class.java).apply {
            putExtra(SearchResultsActivity.EXTRA_QUERY, query)
        }
        startActivity(intent)
    }
}
