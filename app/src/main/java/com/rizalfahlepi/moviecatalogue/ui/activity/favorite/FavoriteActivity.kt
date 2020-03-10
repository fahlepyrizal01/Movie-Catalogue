package com.rizalfahlepi.moviecatalogue.ui.activity.favorite

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.ui.adapter.FavoriteViewPagerAdapter
import com.rizalfahlepi.moviecatalogue.ui.fragment.favorite.FavoriteFragment
import kotlinx.android.synthetic.main.activity_favorite.*

class FavoriteActivity : AppCompatActivity(), FavoriteFragment.LoadDataListener {

    private var finishLoadMovie = false
    private var finishLoadTVSeries = false
    private lateinit var adapter: FavoriteViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        toolbarFavorite.setNavigationOnClickListener { finish() }
        showLoading(true)

        adapter = FavoriteViewPagerAdapter(this, supportFragmentManager)
        viewPagerFavorite.adapter = adapter
        tabLayoutFavorite.setupWithViewPager(viewPagerFavorite)
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

    override fun setOnDataChange() {
        adapter.notifyDataSetChanged()
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarFavorite.visibility = View.VISIBLE
        } else {
            progressBarFavorite.visibility = View.GONE
        }
    }
}