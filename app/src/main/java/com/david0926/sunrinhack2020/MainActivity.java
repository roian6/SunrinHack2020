package com.david0926.sunrinhack2020;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.david0926.sunrinhack2020.databinding.ActivityMainBinding;
import com.david0926.sunrinhack2020.fragment.MainFragment1;
import com.david0926.sunrinhack2020.fragment.MainFragment2;
import com.david0926.sunrinhack2020.fragment.MainFragment3;
import com.david0926.sunrinhack2020.fragment.MainFragment4;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private AlarmManager alarmManager;
    private String hour, minute;
    private ActivityMainBinding binding;
    private int alarm_time_hour,alarm_time_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        SharedPreferences pref = getSharedPreferences("alarm_time",MODE_PRIVATE);

        Intent intent1 = getIntent();

        if (intent1.getIntExtra("check",1)==0){
            alarmManager.cancel(pIntent);
        }
        if (pref.getString("alarm_time_hour",null)!=null){

            hour = pref.getString("alarm_time_hour",null);
            minute = pref.getString("alarm_time_minute",null);

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            alarm_time_hour = Integer.parseInt(hour);
            alarm_time_minute = Integer.parseInt(minute);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm_time_hour);
            calendar.set(Calendar.MINUTE, alarm_time_minute);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // 지정한 시간에 매일 알림
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

        }

        if(intent1.getStringExtra("alarm_time_hour")!=null&&intent1.getStringExtra("alarm_time_minute")!=null){

            hour = intent1.getStringExtra("alarm_time_hour");
            minute = intent1.getStringExtra("alarm_time_minute");
            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour));
            calendar.set(Calendar.MINUTE, Integer.parseInt(minute));
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            // 지정한 시간에 매일 알림
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

        }


        binding.fabMain.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ChatActivity.class)));

        binding.bottomnavMain.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.action_1:
                    switchFragment(MainFragment1.newInstance());
                    break;
                case R.id.action_2:
                    switchFragment(MainFragment2.newInstance());
                    break;
                case R.id.action_3:
                    switchFragment(MainFragment3.newInstance());
                    break;
                case R.id.action_4:
                    switchFragment(MainFragment4.newInstance());
                    break;
            }
            return true;
        });
        switchFragment(MainFragment1.newInstance());
    }

    void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.commit();
    }
}