package com.rizalfahlepi.moviecatalogue.ui.activity.main

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.ui.activity.favorite.FavoriteActivity
import com.rizalfahlepi.moviecatalogue.ui.activity.search.SearchActivity
import com.rizalfahlepi.moviecatalogue.ui.activity.setting.SettingActivity
import com.rizalfahlepi.moviecatalogue.ui.adapter.MovieViewPagerAdapter
import com.rizalfahlepi.moviecatalogue.ui.fragment.movie.MovieFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MovieFragment.LoadDataListener {

    private var finishLoadMovie = false
    private var finishLoadTVSeries = false

    companion object {
        const val EXTRA_NOTIFICATION = "extraNotification"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarMain)

        if (intent.hasExtra(EXTRA_NOTIFICATION)){
           val nfm =  getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nfm.cancelAll()
        }

        showLoading(true)

        val adapter = MovieViewPagerAdapter(this, supportFragmentManager)
        viewPagerMovie.adapter = adapter
        tabLayoutMovie.setupWithViewPager(viewPagerMovie)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorites -> startActivity(Intent(this, FavoriteActivity::class.java))
            R.id.action_search -> startActivity(Intent(this, SearchActivity::class.java))
            R.id.action_setting -> startActivity(Intent(this, SettingActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun setOnFinishLoadData(position: Int) {
        when (position){
            0 -> finishLoadMovie = true
            1 -> finishLoadTVSeries = true
        }

        if (finishLoadMovie && finishLoadTVSeries){
            showLoading(false)
            finishLoadMovie = false
            finishLoadTVSeries = false
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarMovie.visibility = View.VISIBLE
        } else {
            progressBarMovie.visibility = View.GONE
        }
    }
}