package com.david0926.sunrinhack2020.fragment;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.sunrinhack2020.LoginActivity;
import com.david0926.sunrinhack2020.MainActivity;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.broadcast;
import com.david0926.sunrinhack2020.databinding.FragmentMain4Binding;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class MainFragment4 extends Fragment {
    int hour;
    int minute;
    int alarm_hour;
    int alarm_minute;

    public static MainFragment4 newInstance() {
        return new MainFragment4();
    }

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

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
        binding.pushNotificationCheck.setChecked(true);
        binding.setUser(UserCache.getUser(mContext));

        SharedPreferences pref = mContext.getSharedPreferences("alarm_time", Context.MODE_PRIVATE);
        if (pref.getInt("alarm_time_hour_int", 0) != 0) {
            binding.timePicker.setHour(pref.getInt("alarm_time_hour_int", 0));
            binding.timePicker.setMinute(pref.getInt("alarm_time_minute_int", 0));
        }

        binding.pushNotificationCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {

                    alarm_hour = pref.getInt("alarm_time_hour_int", 0);
                    alarm_minute = pref.getInt("alarm_time_minute_int", 0);
                    Intent intent1 = new Intent(mContext, broadcast.class);
                    intent1.putExtra("alarm_time_hour", alarm_hour + "");
                    intent1.putExtra("alarm_time_minute", alarm_minute + "");
                    mContext.sendBroadcast(intent1);

                } else {
                    Intent intent2 = new Intent(mContext, MainActivity.class);
                    intent2.putExtra("check", 0);
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
                    Log.e("asd", hourOfDay + "시 " + minute + "분");
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("alarm_time_hour", hourOfDay + "");
                    editor.putString("alarm_time_minute", minute + "");
                    editor.putInt("alarm_time_hour_int", hourOfDay);
                    editor.putInt("alarm_time_minute_int", minute);
                    editor.commit();

                    binding.timePicker.setHour(hourOfDay);
                    binding.timePicker.setMinute(minute);

                    Intent intent1 = new Intent(mContext, broadcast.class);

                    intent1.putExtra("alarm_time_hour", hourOfDay + "");
                    intent1.putExtra("alarm_time_minute", minute + "");
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
                Log.e("asd", "asd");
                TimePickerDialog dialog = new TimePickerDialog(mContext, listener, hour, minute, false);
                dialog.show();
            }

            TimePickerDialog.OnTimeSetListener listener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    Log.e("asd", hourOfDay + "시 " + minute + "분");
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

        binding.settingUserOut.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder
                    .setTitle("회원 탈퇴")
                    .setMessage("정말로 탈퇴하시겠습니까? 이 작업은 복구가 불가능합니다.")
                    .setPositiveButton("탈퇴하기", (dialogInterface, i) -> {
                        FirebaseUser user = firebaseAuth.getCurrentUser();

                        //1. delete user(Firebase Auth)
                        if (user == null) return;
                        user.delete();

                        //2. delete user data (Firestore)
                        firebaseFirestore.collection("users")
                                .document(UserCache.getUser(mContext).getEmail())
                                .delete()
                                .addOnSuccessListener(runnable -> {
                                    Toast.makeText(mContext, "성공적으로 탈퇴했습니다.", Toast.LENGTH_SHORT).show();
                                    getActivity().finishAffinity();
                                });
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {
                    })
                    .create()
                    .show();
        });

        binding.resetPassword.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder
                    .setTitle("비밀번호 재설정")
                    .setMessage("이메일 주소로 비밀번호 재설정 메일이 전송됩니다.")
                    .setPositiveButton("전송하기", (dialogInterface, i) -> {
                        firebaseAuth.sendPasswordResetEmail(UserCache.getUser(mContext).getEmail());
                        firebaseAuth.signOut();
                        UserCache.clear(mContext);
                        startActivity(new Intent(mContext, LoginActivity.class));
                        Toast.makeText(mContext, "성공적으로 전송했습니다.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    })
                    .setNegativeButton("취소", (dialogInterface, i) -> {
                    })
                    .create()
                    .show();
        });
        return binding.getRoot();
    }
}
