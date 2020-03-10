package com.rizalfahlepi.moviecatalogue.ui.activity.search

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.model.Movie
import com.rizalfahlepi.moviecatalogue.ui.activity.detailmovie.DetailMovieActivity
import com.rizalfahlepi.moviecatalogue.ui.adapter.MovieRecyclerViewAdapter
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var movieViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        movieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            SearchViewModel::class.java)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean = false

            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.replace(" ", "").isNotEmpty()){
                    showLoading(true)
                    when(textViewChooseTypeSearch.text){
                        getString(R.string.text_movies) -> {
                            movieViewModel.getMovie().observe(this@SearchActivity, Observer {
                                if (it != null) {
                                    initData( when(textViewChooseTypeSearch.text)
                                    {
                                        getString(R.string.text_movies) -> 0
                                        getString(R.string.text_tv_series) -> 1
                                        else -> 0
                                    }, it)
                                    showLoading(false)
                                }
                            })

                            movieViewModel.setMovies(query) {
                                Toast.makeText(this@SearchActivity, it, Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        }

                        getString(R.string.text_tv_series) -> {
                            movieViewModel.getTVSeries().observe(this@SearchActivity, Observer {
                                if (it != null) {
                                    initData( when(textViewChooseTypeSearch.text)
                                    {
                                        getString(R.string.text_movies) -> 0
                                        getString(R.string.text_tv_series) -> 1
                                        else -> 0
                                    }, it)
                                    showLoading(false)
                                }
                            })

                            movieViewModel.setTVSeries(query) {
                                Toast.makeText(this@SearchActivity, it, Toast.LENGTH_SHORT).show()
                                showLoading(false)
                            }
                        }
                    }
                }
                return false
            }
        })

        searchView.isIconifiedByDefault = false
        searchView.requestFocus()

        imageViewBack.setOnClickListener(this)
        cardViewSearch.setOnClickListener(this)
        textViewChooseTypeSearch.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            imageViewBack -> finish()

            cardViewSearch -> searchView.requestFocus()

            textViewChooseTypeSearch -> {
                val arrayType = arrayOf(getString(R.string.text_movies), getString(R.string.text_tv_series))
                AlertDialog
                    .Builder(this)
                    .setTitle(getString(R.string.text_select_search_type))
                    .setItems(arrayType) { _, position ->
                        textViewChooseTypeSearch.text = arrayType[position]
                        if (searchView.query.toString().replace(" ", "").isNotEmpty()){
                            showLoading(true)
                            when(textViewChooseTypeSearch.text){
                                getString(R.string.text_movies) -> {
                                    movieViewModel.getMovie().observe(this@SearchActivity, Observer {
                                        if (it != null) {
                                            initData( when(textViewChooseTypeSearch.text)
                                            {
                                                getString(R.string.text_movies) -> 0
                                                getString(R.string.text_tv_series) -> 1
                                                else -> 0
                                            }, it)
                                            showLoading(false)
                                        }
                                    })

                                    movieViewModel.setMovies(searchView.query.toString()) {
                                        Toast.makeText(this@SearchActivity, it, Toast.LENGTH_SHORT).show()
                                        showLoading(false)
                                    }
                                }

                                getString(R.string.text_tv_series) -> {
                                    movieViewModel.getTVSeries().observe(this@SearchActivity, Observer {
                                        if (it != null) {
                                            initData( when(textViewChooseTypeSearch.text)
                                            {
                                                getString(R.string.text_movies) -> 0
                                                getString(R.string.text_tv_series) -> 1
                                                else -> 0
                                            }, it)
                                            showLoading(false)
                                        }
                                    })

                                    movieViewModel.setTVSeries(searchView.query.toString()) {
                                        Toast.makeText(this@SearchActivity, it, Toast.LENGTH_SHORT).show()
                                        showLoading(false)
                                    }
                                }
                            }
                        }
                    }
                    .setNegativeButton(getString(R.string.text_close)){ _, _ ->}
                    .create().show()
            }
        }
    }

    private fun initData(type: Int, data: ArrayList<Movie>){
        val movieAdapter = MovieRecyclerViewAdapter(data, type)
        with(recyclerViewSearch){
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = movieAdapter
        }

        movieAdapter.setOnItemClickCallback(object: MovieRecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(movieID: Int) {
                startActivity(
                    Intent(this@SearchActivity, DetailMovieActivity::class.java)
                        .putExtra(DetailMovieActivity.EXTRA_TYPE, type)
                        .putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, movieID))
            }
        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarSearch.visibility = View.VISIBLE
        } else {
            progressBarSearch.visibility = View.GONE
        }
    }
}
