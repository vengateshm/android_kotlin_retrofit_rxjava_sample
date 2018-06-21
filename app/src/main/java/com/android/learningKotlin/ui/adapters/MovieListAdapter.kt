package com.android.learningKotlin.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.learningKotlin.R
import com.android.learningKotlin.models.Movie
import com.android.learningKotlin.ui.adapters.viewHolders.MovieVH

class MovieListAdapter : RecyclerView.Adapter<MovieVH>() {

    private var moviesList: List<Movie> = ArrayList()

    fun setMovies(moviesList: List<Movie>) {
        this.moviesList = moviesList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieVH {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.movie_list_item, parent, false)
        return MovieVH(view)
    }

    override fun onBindViewHolder(holder: MovieVH, position: Int) {
        val movie = moviesList[position]
        holder.setMovie(movie)
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}