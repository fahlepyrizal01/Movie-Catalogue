package com.rizalfahlepi.myfavoritemovies.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rizalfahlepi.myfavoritemovies.R
import com.rizalfahlepi.myfavoritemovies.ui.adapter.FavoriteViewPagerAdapter
import com.rizalfahlepi.myfavoritemovies.ui.fragment.FavoriteFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FavoriteFragment.LoadDataListener {

    private var finishLoadMovie = false
    private var finishLoadTVSeries = false
    private lateinit var adapter: FavoriteViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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