package com.david0926.sunrinhack2020.fragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.sunrinhack2020.Alarm;
import com.david0926.sunrinhack2020.LoginActivity;
import com.david0926.sunrinhack2020.MainActivity;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.broadcast;
import com.david0926.sunrinhack2020.databinding.FragmentMain4Binding;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class MainFragment4 extends Fragment {
    int hour;
    int minute;
    int alarm_hour;
    int alarm_minute;

    public static MainFragment4 newInstance() {
        return new MainFragment4();
    }

    private Context mContext;
    private FragmentMain4Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main4, container, false);

        binding.setUser(UserCache.getUser(mContext));

        SharedPreferences pref = mContext.getSharedPreferences("alarm_time",Context.MODE_PRIVATE);

        binding.pushNotificationCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){

                    alarm_hour = pref.getInt("alarm_time_hour_int",0);
                    alarm_minute = pref.getInt("alarm_time_minute_int",0);
                    Intent intent1 = new Intent(mContext, broadcast.class);
                    intent1.putExtra("alarm_time_hour",alarm_hour+"");
                    intent1.putExtra("alarm_time_minute",alarm_minute+"");
                    mContext.sendBroadcast(intent1);

                }else{
                    Intent intent2 = new Intent(mContext, MainActivity.class);
                    intent2.putExtra("check",0);
                    mContext.sendBroadcast(intent2);

                }
            }
        });
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(mContext, listener, hour, minute, false);
                dialog.show();
            }
            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Log.e("asd",hourOfDay + "시 " + minute + "분");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("alarm_time_hour",hourOfDay+"");
                    editor.putString("alarm_time_minute",minute+"");
                    editor.putInt("alarm_time_hour_int",hourOfDay);
                    editor.putInt("alarm_time_minute_int",minute);
                    editor.commit();

                    Intent intent1 = new Intent(mContext, broadcast.class);

                    intent1.putExtra("alarm_time_hour",hourOfDay+"");
                    intent1.putExtra("alarm_time_minute",minute+"");
                    mContext.sendBroadcast(intent1);
                }
            };
        });

//        binding.clickView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.e("asd","asd123");
//            }
//        });
//
        binding.cardMain4Picker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("asd","asd");
                TimePickerDialog dialog = new TimePickerDialog(mContext, listener, hour, minute, false);
               dialog.show();
            }
            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Log.e("asd",hourOfDay + "시 " + minute + "분");
                }
            };
        });




        binding.logOut.setOnClickListener(view -> {
            UserCache.clear(mContext);
            Activity activity = getActivity();
            if (activity == null) return;
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(mContext, LoginActivity.class));
            activity.finish();
        });

        return binding.getRoot();
    }
}
