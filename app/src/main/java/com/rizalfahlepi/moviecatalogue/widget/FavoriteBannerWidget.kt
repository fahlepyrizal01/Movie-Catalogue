package com.rizalfahlepi.moviecatalogue.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.service.StackWidgetService
import com.rizalfahlepi.moviecatalogue.ui.activity.detailmovie.DetailMovieActivity

class FavoriteBannerWidget : AppWidgetProvider() {

    companion object {
        private const val CLICK_ACTION = "com.rizalfahlepi.moviecatalogue.CLICK_ACTION"
        const val EXTRA_MOVIE_ID = "com.rizalfahlepi.moviecatalogue.EXTRA_ITEM"
        const val EXTRA_MOVIE_TYPE = "com.rizalfahlepi.moviecatalogue.EXTRA_TYPE"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.favorite_banner_widget)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, FavoriteBannerWidget::class.java)
            toastIntent.action = CLICK_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            if (intent.action == CLICK_ACTION) {
                val intentDetail = Intent(context, DetailMovieActivity::class.java)
                intentDetail.putExtra(DetailMovieActivity.EXTRA_MOVIE_ID, intent.getIntExtra(EXTRA_MOVIE_ID, 0))
                intentDetail.putExtra(DetailMovieActivity.EXTRA_TYPE, intent.getIntExtra(EXTRA_MOVIE_TYPE, 0))
                intentDetail.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intentDetail)
            }
        }
    }
}