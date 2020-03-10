package com.rizalfahlepi.moviecatalogue.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.api.TheMovieDBApi
import com.rizalfahlepi.moviecatalogue.database.DatabaseContract
import com.rizalfahlepi.moviecatalogue.utils.MappingHelper
import com.rizalfahlepi.moviecatalogue.widget.FavoriteBannerWidget

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()
    private val moviesID =  ArrayList<Int>()
    private val moviesType =  ArrayList<Int>()

    override fun onCreate() {}

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        try {
            val cursor = mContext.contentResolver.query(DatabaseContract.FavoriteColumns.CONTENT_URI, null, null, null, null)
            val movies = MappingHelper.mapCursorToArrayList(cursor)
            for (data in movies){
                try {
                    val bitmap: Bitmap = Glide.with(mContext)
                        .asBitmap()
                        .load(TheMovieDBApi.getPosterMovie(data.posterPath ?: ""))
                        .submit(512, 512)
                        .get()

                    mWidgetItems.add(bitmap)
                    moviesID.add(data.idMovie ?: 0)
                    moviesType.add(data.type ?: 0)
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }

            cursor?.close()
        }catch (e: Exception){
            e.printStackTrace()
        } finally {
            Binder.restoreCallingIdentity(identityToken)
        }
    }

    override fun onDestroy() {}

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val fillInIntent = Intent()
        fillInIntent.putExtras(bundleOf(FavoriteBannerWidget.EXTRA_MOVIE_ID to moviesID[position]))
        fillInIntent.putExtras(bundleOf(FavoriteBannerWidget.EXTRA_MOVIE_TYPE to moviesType[position]))

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}