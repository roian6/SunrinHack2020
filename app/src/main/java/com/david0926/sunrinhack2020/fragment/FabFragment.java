package com.david0926.sunrinhack2020.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import androidx.databinding.DataBindingUtil;

import com.allattentionhere.fabulousfilter.AAH_FabulousFragment;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.databinding.FragmentFabBinding;

import org.jetbrains.annotations.NotNull;

public class FabFragment extends AAH_FabulousFragment {

    public static FabFragment newInstance() {
        return new FabFragment();
    }

    private Context mContext;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        FragmentFabBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.fragment_fab, null, false);

        binding.btnFragfabUpload.setOnClickListener(view -> {
            closeFilter("close");
        });

        setShowsDialog(true);
        setAnimationDuration(400);
        setPeekHeight(250);
        setViewgroupStatic(binding.constraintFabBottom);
        setViewMain(binding.relativeFabContent);
        setMainContentView(binding.getRoot());
        super.setupDialog(dialog, style);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().sendBroadcast(new Intent("invalidate_fab"));
    }
}
