package com.david0926.sunrinhack2020.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.builders.DatePickerBuilder;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.applandeo.materialcalendarview.listeners.OnSelectDateListener;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.databinding.FragmentMain2Binding;
import com.david0926.sunrinhack2020.util.SharedPreferenceUtil;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainFragment2 extends Fragment {
    private CalendarView calendarView;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;

    public static MainFragment2 newInstance() {
        return new MainFragment2();
    }

    private Context mContext;
    private FragmentMain2Binding binding;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main2, container, false);

        binding.calendarView.setOnDayClickListener(eventDay -> {
            calendar = eventDay.getCalendar();

//                day = calendar.get(Calendar.DAY_OF_MONTH);
//                month = calendar.get(Calendar.MONTH)+1;
//                year = calendar.get(Calendar.YEAR);

            List<EventDay> events = new ArrayList<>();
            events.add(new EventDay(calendar, R.drawable.ic_baseline_devices_24, Color.parseColor("#228B22")));
            binding.calendarView.setEvents(events);

        });

        return binding.getRoot();
    }
}
