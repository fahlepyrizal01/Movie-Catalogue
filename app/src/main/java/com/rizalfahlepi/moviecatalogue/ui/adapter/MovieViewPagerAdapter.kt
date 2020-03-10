package com.rizalfahlepi.moviecatalogue.ui.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.ui.fragment.movie.MovieFragment

class MovieViewPagerAdapter(private val context: Context?, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.text_movies, R.string.text_tv_series)

    override fun getItem(position: Int): Fragment {
        return MovieFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context?.resources?.getString(tabTitles[position])
    }

    override fun getCount(): Int = tabTitles.size
}