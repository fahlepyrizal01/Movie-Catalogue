package com.rizalfahlepi.moviecatalogue.ui.activity.detailmovie

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.api.TheMovieDBApi
import com.rizalfahlepi.moviecatalogue.model.Movie
import com.rizalfahlepi.moviecatalogue.ui.activity.search.SearchActivity
import kotlinx.android.synthetic.main.activity_detail_movie.*
import java.text.SimpleDateFormat
import java.util.*

class DetailMovieActivity : AppCompatActivity() {

    private var menuItem: Menu? = null
    private var isFavorite = false
    private lateinit var detailMovieViewModel: DetailMovieViewModel
    private lateinit var movie: Movie

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_MOVIE_ID = "extra_movie_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)

        setSupportActionBar(toolbarDetail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        toolbarDetail.setNavigationOnClickListener { finish() }

        detailMovieViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(DetailMovieViewModel::class.java)

        if (intent.hasExtra(EXTRA_MOVIE_ID) && intent.hasExtra(EXTRA_TYPE)){
            showLoading(true)

            favoriteState()

            when(intent.getIntExtra(EXTRA_TYPE, 0)){
                0 -> {
                    detailMovieViewModel.getDetailMovie().observe(this as LifecycleOwner, Observer {
                        if (it != null) {
                            movie = it
                            initData(intent.getIntExtra(EXTRA_TYPE, 0), it)
                        }

                        showLoading(false)
                    })

                    detailMovieViewModel.setDetailMovie(intent.getIntExtra(EXTRA_MOVIE_ID, 0)) {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }
                1 -> {
                    detailMovieViewModel.getDetailTVSeries().observe(this as LifecycleOwner, Observer {
                        if (it != null) {
                            movie = it
                            initData(intent.getIntExtra(EXTRA_TYPE, 1), it)
                        }

                        showLoading(false)
                    })

                    detailMovieViewModel.setDetailTVSeries(intent.getIntExtra(EXTRA_MOVIE_ID, 0)) {
                        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        showLoading(false)
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        menuItem = menu
        setFavorite()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_favorites -> if (isFavorite) removeFromFavorite() else addToFavorite()
            R.id.action_search -> startActivity(Intent(this, SearchActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initData(type: Int, data: Movie){
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val simpleDateFormatView = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

        Glide.with(this).load(TheMovieDBApi.getPosterMovie(data.posterPath ?: "")).into(imageViewPosterValue)
        textViewIDValue.text = data.id.toString()
        textViewVoteAverageValue.text = data.voteAverage.toString()
        textViewVoteCountValue.text = data.voteCount.toString()
        textViewLanguageValue.text = try {
            Locale(data.originalLanguage ?: "").displayLanguage
        }catch (e: Exception){
            e.printStackTrace()
            data.originalLanguage?.toUpperCase(Locale.getDefault())
        }
        textViewPopularityValue.text = data.popularity.toString()
        textViewOverviewValue.text = data.overview

        when(type){
            0 -> {
                textViewTitleValue.text = data.title
                textViewDateValue.text = try {
                    simpleDateFormatView.format(simpleDateFormat.parse(data.releaseDate ?: "") ?: Date())
                }catch (e: Exception){
                    e.printStackTrace()
                    data.releaseDate
                }
                val runTime = "${data.runtime} ${getString(R.string.text_minute)}"
                textViewRunTimeValue.text = runTime
            }

            1 -> {
                textViewTitleValue.text = data.name
                textViewDateValue.text = try {
                    simpleDateFormatView.format(simpleDateFormat.parse(data.firstAirDate ?: "") ?: Date())
                }catch (e: Exception){
                    e.printStackTrace()
                    data.firstAirDate
                }
                val runTime = "${data.episodeRunTime} ${getString(R.string.text_minute)} /${getString(R.string.text_episode)}"
                textViewRunTimeValue.text = runTime
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarDetailMovie.visibility = View.VISIBLE
        } else {
            progressBarDetailMovie.visibility = View.GONE
        }
    }

    private fun favoriteState(){
        detailMovieViewModel.getFavorite().observe(this as LifecycleOwner, Observer {
            if (it != null) {
                isFavorite = it
            }
        })

        detailMovieViewModel.checkFavorite(intent.getIntExtra(EXTRA_MOVIE_ID, 0), this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addToFavorite(){
        detailMovieViewModel.insertFavorite(movie, intent.getIntExtra(EXTRA_TYPE, 0), this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == getString(R.string.text_added_to_favorite)){
                isFavorite = true
                setFavorite()
            }
            showLoading(false)
        }
    }

    private fun removeFromFavorite(){
        detailMovieViewModel.removeFavorite(intent.getIntExtra(EXTRA_MOVIE_ID, 0), this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            if (it == getString(R.string.text_removed_from_favorite)){
                isFavorite = false
                setFavorite()
            }
            showLoading(false)
        }
    }

    private fun setFavorite() {
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_added)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_favorite_add)
    }
}