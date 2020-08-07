package com.david0926.sunrinhack2020.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.sunrinhack2020.LoginActivity;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.databinding.FragmentMain4Binding;
import com.david0926.sunrinhack2020.model.UserModel;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class MainFragment4 extends Fragment {

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

        UserModel userModel = UserCache.getUser(mContext);
        binding.setSettinguser(userModel);
        binding.setSettingusername(userModel.getName());
        binding.setSettinguseremail(userModel.getEmail());



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
