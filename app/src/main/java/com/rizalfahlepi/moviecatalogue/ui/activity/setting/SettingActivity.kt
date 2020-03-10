package com.rizalfahlepi.moviecatalogue.ui.activity.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.rizalfahlepi.moviecatalogue.R
import com.rizalfahlepi.moviecatalogue.service.AlarmReceiverService
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var alarmReceiver: AlarmReceiverService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        toolbarSetting.setNavigationOnClickListener { finish() }

        alarmReceiver = AlarmReceiverService()

        if (alarmReceiver.isAlarmSet(this, AlarmReceiverService.TYPE_REPEATING_DAILY)){
            switchDaily.isChecked = true
        }

        if (alarmReceiver.isAlarmSet(this, AlarmReceiverService.TYPE_REPEATING_RELEASE_TODAY)){
            switchReleaseToday.isChecked = true
        }

        switchDaily.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiverService.TYPE_REPEATING_DAILY,
                    "07:00", getString(R.string.text_catalogue_movie_missing_you))
            }else{
                alarmReceiver.cancelAlarm(this, AlarmReceiverService.TYPE_REPEATING_DAILY)
            }
        }

        switchReleaseToday.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                alarmReceiver.setRepeatingAlarm(this, AlarmReceiverService.TYPE_REPEATING_RELEASE_TODAY,
                    "08:00", "")
            }else{
                alarmReceiver.cancelAlarm(this, AlarmReceiverService.TYPE_REPEATING_RELEASE_TODAY)
            }
        }

        textViewLanguage.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v){
            textViewLanguage -> startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }
}
