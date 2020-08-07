package com.david0926.sunrinhack2020.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.david0926.sunrinhack2020.LoginActivity;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.databinding.FragmentMain4Binding;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class MainFragment4 extends Fragment {

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

        binding.setUser(UserCache.getUser(mContext));

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
