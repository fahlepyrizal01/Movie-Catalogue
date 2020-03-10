package com.rizalfahlepi.moviecatalogue.service

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.api.TheMovieDBApi
import com.rizalfahlepi.moviecatalogue.ui.activity.main.MainActivity
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class AlarmReceiverService: BroadcastReceiver() {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    companion object {
        const val TYPE_REPEATING_DAILY = "RepeatingDailyAlarm"
        const val TYPE_REPEATING_RELEASE_TODAY = "RepeatingReleaseTodayAlarm"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"

        private const val ID_REPEATING_DAILY = 100
        private const val ID_REPEATING_RELEASE_TODAY = 101

        private const val NOTIFICATION_REQUEST_CODE = 200
    }

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)

        if (type.equals(TYPE_REPEATING_DAILY, ignoreCase = true)){
            val title = context.getString(R.string.text_daily_reminder)
            val message = intent.getStringExtra(EXTRA_MESSAGE)
            val notifyId = ID_REPEATING_DAILY

            showAlarmNotification(context, title, message ?: "", notifyId)
        } else{
            val client = AsyncHttpClient()
            client.get(TheMovieDBApi.getReleaseMovies(simpleDateFormat.format(Date())), object : AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                    try {
                        val result = String(responseBody)
                        val responseObject = JSONObject(result)
                        val list = responseObject.getJSONArray("results")

                        for (i in 0 until list.length()) {
                            val movie = list.getJSONObject(i)
                            val title = movie.getString("title")
                            val message = "${movie.getString("title")} ${context.getString(R.string.text_has_been_release_today)}"
                            val notifyId = ID_REPEATING_RELEASE_TODAY

                            showAlarmNotification(context, title, message, notifyId+(0..100).random())
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message ?: "Something Wrong", Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                }

                override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
                    Toast.makeText(
                        context,
                        when (statusCode) {
                            401 -> "$statusCode : Bad Request"
                            403 -> "$statusCode : Forbidden"
                            404 -> "$statusCode : Not Found"
                            else -> "$statusCode : ${error?.message}"
                        },
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifyId: Int) {

        val channelID = "Channel_1"
        val channelName = "AlarmManager channel"

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra(MainActivity.EXTRA_NOTIFICATION, true)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(context, NOTIFICATION_REQUEST_CODE, intent, PendingIntent.FLAG_ONE_SHOT)

        val builder = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setContentIntent(pendingIntent)
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(channelID,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(channelID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifyId, notification)
    }

    fun setRepeatingAlarm(context: Context, type: String, time: String, message: String) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiverService::class.java)
        intent.putExtra(EXTRA_MESSAGE, message)
        intent.putExtra(EXTRA_TYPE, type)

        val timeArray = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, if (type.equals(TYPE_REPEATING_DAILY, ignoreCase = true)) ID_REPEATING_DAILY else ID_REPEATING_RELEASE_TODAY, intent, 0)
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }


    fun cancelAlarm(context: Context, type: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiverService::class.java)
        val requestCode = if (type.equals(TYPE_REPEATING_DAILY, ignoreCase = true)) ID_REPEATING_DAILY else ID_REPEATING_RELEASE_TODAY
        val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0)
        pendingIntent.cancel()

        alarmManager.cancel(pendingIntent)
    }


    fun isAlarmSet(context: Context, type: String): Boolean {
        val intent = Intent(context, AlarmReceiverService::class.java)
        val requestCode = if (type.equals(TYPE_REPEATING_DAILY, ignoreCase = true)) ID_REPEATING_DAILY else ID_REPEATING_RELEASE_TODAY

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null
    }
}