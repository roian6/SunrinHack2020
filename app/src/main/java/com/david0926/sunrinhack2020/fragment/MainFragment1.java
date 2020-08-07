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
import com.david0926.sunrinhack2020.model.ChatModel;
import com.david0926.sunrinhack2020.model.DiaryModel;
import com.david0926.sunrinhack2020.model.UserModel;
import com.david0926.sunrinhack2020.util.LinearLayoutManagerWrapper;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.platforminfo.DefaultUserAgentPublisher;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class MainFragment1 extends Fragment {

    public static MainFragment1 newInstance() {
        return new MainFragment1();
    }

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

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

        adapter.setOnItemClickListener((view, item) -> {
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh(){
        diaryItems.clear();
        firebaseFirestore
                .collection("users")
                .document(UserCache.getUser(mContext).getEmail())
                .get()
                .addOnSuccessListener(runnable -> {
                    ArrayList<ChatModel> chatList = runnable.toObject(UserModel.class).getChat();
                    if (chatList.isEmpty()) return;

                    for(int i=0;i<chatList.size()-1;i+=2){
                        DiaryModel model = new DiaryModel();
                        model.setQuestion(chatList.get(i).getText());
                        model.setAnswer(chatList.get(i+1).getText());
                        model.setTime(chatList.get(i+1).getTime());
                        model.setImage(chatList.get(i+1).getImage());
                        diaryItems.add(model);
                    }

                });
    }
}
