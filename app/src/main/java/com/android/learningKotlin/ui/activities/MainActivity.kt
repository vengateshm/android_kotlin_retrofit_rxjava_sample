package com.android.learningKotlin.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.learningKotlin.R
import com.android.learningKotlin.network.ApiClient
import com.android.learningKotlin.ui.adapters.MovieListAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainActivity : AppCompatActivity() {

    // lateinit can be applied only for var not val(since val is immutable)
    private lateinit var rvList: RecyclerView
    private lateinit var tvNoRecords: TextView
    private lateinit var progressBar: ProgressBar

    private lateinit var movieListAdapter: MovieListAdapter
    private lateinit var moviesDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViews()
        setRecyclerView()

        progressBar.visibility = VISIBLE
        getMovies()
    }

    // Find views by ids
    private fun findViews() {
        rvList = findViewById(R.id.rvList)
        tvNoRecords = findViewById(R.id.tvNoRecords)
        progressBar = findViewById(R.id.progressBar)
    }

    private fun setRecyclerView() {
        val llm = LinearLayoutManager(this)
        rvList.layoutManager = llm
        rvList.setHasFixedSize(true)

        movieListAdapter = MovieListAdapter()
        rvList.adapter = movieListAdapter
    }

    private fun getMovies() {
        moviesDisposable = ApiClient.getMoviesApi()
                .getTopRatedMovies(getString(R.string.moviedb_api_key),
                        "en_US", 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { topRatedMovies ->
                            progressBar.visibility = GONE
                            movieListAdapter.setMovies(topRatedMovies.moviesList)
                            movieListAdapter.notifyDataSetChanged()
                        },
                        { throwable ->
                            progressBar.visibility = GONE
                            Toast.makeText(this, throwable.message, Toast.LENGTH_SHORT).show()
                        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!moviesDisposable.isDisposed) {
            moviesDisposable.dispose()
        }
    }
}
