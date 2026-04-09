package com.pellicola.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pellicola.R
import com.pellicola.models.Movie
import com.squareup.picasso.Picasso

class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView  = view.findViewById(R.id.imgPoster)
        val title: TextView    = view.findViewById(R.id.tvTitle)
        val year: TextView     = view.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.title.text = movie.title
        holder.year.text  = movie.year

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
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
