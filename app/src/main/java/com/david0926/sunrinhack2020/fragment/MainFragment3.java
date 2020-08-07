package com.david0926.sunrinhack2020.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.databinding.FragmentMain3Binding;
import com.david0926.sunrinhack2020.model.UserModel;
import com.david0926.sunrinhack2020.util.UserCache;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainFragment3 extends Fragment {

    public static MainFragment3 newInstance() {
        return new MainFragment3();
    }

    private Context mContext;
    private FragmentMain3Binding binding;
    String during_day;
    String now_day;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main3, container, false);

        UserModel userModel = UserCache.getUser(mContext);
        binding.setUser(userModel);

        int a = userModel.getChat().size() / 2;

        now_day = a + "";
        binding.setNowduringday(now_day + "편 작성했어요");

        long now = System.currentTimeMillis();
        Date mDate = new Date(now);
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String getTime = simpleDate.format(mDate);
        during_day = calDateBetweenAandB(getTime, userModel.getTime());
        binding.setDuringdaytext(during_day + "일째");

        binding.setDuringday(userModel.getTime());

        return binding.getRoot();
    }


    public static String calDateBetweenAandB(String date1, String date2) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/mm/dd", Locale.getDefault());
            Date FirstDate = format.parse(date1);
            Date SecondDate = format.parse(date2);

            long calDate = FirstDate.getTime() - SecondDate.getTime();

            long calDateDays = calDate / (24 * 60 * 60 * 1000);

            calDateDays = Math.abs(calDateDays);

            String calDateDaysString = calDateDays + "";

            return calDateDaysString;

        } catch (ParseException e) {
            return null;
        }
    }
}


//        binding.btnMain3Profile.setOnClickListener(view -> {
//            UserModel model = UserCache.getUser(mContext);
//            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//            builder.setTitle("My Profile").setMessage("Username: " + model.getName()
//                    + "\nEmail: " + model.getEmail() + "\nSign Up: " + model.getTime());
//            builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
//            }).show();
//        });
//
//        binding.btnMain3Share.setOnClickListener(view -> {
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "Wanna join SafePad? \n" +
//                    "https://github.com/roian6/SafePad");
//            sendIntent.setType("text/plain");
//
//            Intent shareIntent = Intent.createChooser(sendIntent, null);
//            startActivity(shareIntent);
//        });
//
//        binding.btnMain3Logout.setOnClickListener(view -> {
//            UserCache.clear(mContext);
//            Activity activity = getActivity();
//            if (activity == null) return;
//            FirebaseAuth.getInstance().signOut();
//            startActivity(new Intent(mContext, LoginActivity.class));
//            activity.finish();
//        });
