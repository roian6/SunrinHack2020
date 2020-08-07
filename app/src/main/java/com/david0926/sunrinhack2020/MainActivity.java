package com.david0926.sunrinhack2020;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

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
    private int hour = 21, minute = 0;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        Intent intent = new Intent(getApplicationContext(), Alarm.class);
        PendingIntent pIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);

        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 지정한 시간에 매일 알림
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pIntent);

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