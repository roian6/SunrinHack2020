package com.david0926.sunrinhack2020.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;

import com.applandeo.materialcalendarview.EventDay;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.databinding.FragmentMain2Binding;
import com.david0926.sunrinhack2020.model.ChatModel;
import com.david0926.sunrinhack2020.model.DiaryModel;
import com.david0926.sunrinhack2020.model.UserModel;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainFragment2 extends Fragment {
    private CalendarView calendarView;
    private Calendar calendar;
    private int day;
    private int month;
    private int year;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private ObservableArrayList<DiaryModel> diaryItems = new ObservableArrayList<>();
    private ArrayList<String> dates = new ArrayList<>();

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

        firebaseFirestore
                .collection("users")
                .document(UserCache.getUser(mContext).getEmail())
                .get()
                .addOnSuccessListener(runnable -> {
                    ArrayList<ChatModel> chatList = runnable.toObject(UserModel.class).getChat();
                    if (chatList.isEmpty()) return;

                    for (int i = 0; i < chatList.size() - 1; i += 2) {
                        DiaryModel model = new DiaryModel();
                        model.setQuestion(chatList.get(i).getText());
                        model.setAnswer(chatList.get(i + 1).getText());
                        model.setTime(chatList.get(i + 1).getTime());
                        model.setImage(chatList.get(i + 1).getImage());
                        diaryItems.add(model);
                    }

                    for (DiaryModel m : diaryItems) {
                        String date = m.getTime().split(" ")[0]; //HH/mm/ss
                        if(!dates.contains(date)) dates.add(date);
                    }
                });

        return binding.getRoot();
    }
}
