package com.rizalfahlepi.myfavoritemovies.ui.adapter

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.rizalfahlepi.myfavoritemovies.R
import com.rizalfahlepi.myfavoritemovies.ui.fragment.FavoriteFragment

class FavoriteViewPagerAdapter(private val context: Context?, fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val tabTitles = intArrayOf(R.string.text_movies, R.string.text_tv_series)

    override fun getItem(position: Int): Fragment {
        return FavoriteFragment.newInstance(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context?.resources?.getString(tabTitles[position])
    }

    override fun getCount(): Int = tabTitles.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}