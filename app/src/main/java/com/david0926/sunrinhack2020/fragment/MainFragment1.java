package com.david0926.sunrinhack2020.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.adapter.DiaryAdapter;
import com.david0926.sunrinhack2020.databinding.FragmentMain1Binding;
import com.david0926.sunrinhack2020.model.DiaryModel;
import com.david0926.sunrinhack2020.util.LinearLayoutManagerWrapper;

import org.jetbrains.annotations.NotNull;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    private Context mContext;
    private ObservableArrayList<DiaryModel> diaryItems = new ObservableArrayList<>();

    private FragmentMain1Binding binding;

    private MainFragment1() {
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main1, container, false);

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                mContext, LinearLayoutManager.HORIZONTAL, false);
        binding.recyclerMain1.setLayoutManager(wrapper);

        DiaryAdapter adapter = new DiaryAdapter();
        binding.recyclerMain1.setAdapter(adapter);
        binding.setDiaryList(diaryItems);

        DiaryModel model = new DiaryModel();
        model.setQuestion("밥?");
        model.setAnswer("ㅇㅇ");
        model.setTime("2020/08/08 12:00:00");

        diaryItems.add(model);

        DiaryModel model2 = new DiaryModel();
        model2.setQuestion("밥2?");
        model2.setAnswer("ㅇㅇ2");
        model2.setTime("2020/08/08 12:00:00");

        diaryItems.add(model2);

        adapter.setOnItemClickListener((view, item) -> {
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        return binding.getRoot();
    }
}
