package com.david0926.sunrinhack2020;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.david0926.sunrinhack2020.adapter.ChatAdapter;
import com.david0926.sunrinhack2020.databinding.ActivityChatBinding;
import com.david0926.sunrinhack2020.model.ChatModel;
import com.david0926.sunrinhack2020.model.UserModel;
import com.david0926.sunrinhack2020.util.LinearLayoutManagerWrapper;
import com.david0926.sunrinhack2020.util.UserCache;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;

public class ChatActivity extends AppCompatActivity {

    private ObservableArrayList<ChatModel> chatItems = new ObservableArrayList<>();
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private ActivityChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        binding.setMessage("");

        binding.toolbarChat.setNavigationOnClickListener(view -> finish());

        LinearLayoutManagerWrapper wrapper = new LinearLayoutManagerWrapper(
                this, LinearLayoutManager.VERTICAL, false);
        binding.recyclerChat.setLayoutManager(wrapper);

        ChatAdapter adapter = new ChatAdapter();
        binding.recyclerChat.setAdapter(adapter);
        binding.setChatList(chatItems);

        adapter.setOnItemClickListener((view, item) -> {
        });
        adapter.setOnItemLongClickListener((view, item) -> true);

        //scroll to bottom when keyboard up
        new TedKeyboardObserver(this).listen(isShow -> {
            binding.recyclerChat.smoothScrollToPosition(chatItems.size()-1);
        });

        refreshChat();

        binding.btnChatSend.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.edtChatMessage.getText())) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("author", UserCache.getUser(this).getName());
            map.put("text", binding.getMessage());
            map.put("time", timeNow());

            firebaseFirestore
                    .collection("users")
                    .document(UserCache.getUser(this).getEmail())
                    .update("chat", FieldValue.arrayUnion(map))
                    .addOnSuccessListener(runnable -> {
                        Gson gson = new Gson();
                        JsonElement e = gson.toJsonTree(map);
                        ChatModel model = gson.fromJson(e, ChatModel.class);
                        chatItems.add(model);
                        binding.setMessage("");
                        binding.recyclerChat.smoothScrollToPosition(chatItems.size()-1);
                    });
        });
    }

    private void refreshChat() {
        chatItems.clear();
        firebaseFirestore
                .collection("users")
                .document(UserCache.getUser(this).getEmail())
                .get()
                .addOnSuccessListener(runnable -> {
                    chatItems.addAll(runnable.toObject(UserModel.class).getChat());
                    //Collections.reverse(chatItems);
                });
    }

    private String timeNow() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).format(new Date());
    }

}