package com.pellicola.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pellicola.R
import com.pellicola.models.MovieDetail
import com.squareup.picasso.Picasso

class FavoritesAdapter(
    private var movies: MutableList<MovieDetail>,
    private val onMovieClick: (MovieDetail) -> Unit,
    private val onRemoveClick: (MovieDetail) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView      = view.findViewById(R.id.imgPoster)
        val title: TextView        = view.findViewById(R.id.tvTitle)
        val year: TextView         = view.findViewById(R.id.tvYear)
        val btnRemove: ImageButton = view.findViewById(R.id.btnRemoveFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.year.text  = "${movie.year} · ${movie.genre}"

        if (movie.poster != "N/A" && movie.poster.isNotEmpty()) {
            Picasso.get()
                .load(movie.poster)
                .placeholder(R.drawable.ic_movie_placeholder)
                .error(R.drawable.ic_movie_placeholder)
                .into(holder.poster)
        } else {
            holder.poster.setImageResource(R.drawable.ic_movie_placeholder)
        }

        holder.itemView.setOnClickListener { onMovieClick(movie) }
        holder.btnRemove.setOnClickListener { onRemoveClick(movie) }
    }

    override fun getItemCount() = movies.size

    fun removeItem(movie: MovieDetail) {
        val index = movies.indexOfFirst { it.imdbId == movie.imdbId }
        if (index != -1) {
            movies.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateMovies(newMovies: MutableList<MovieDetail>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
