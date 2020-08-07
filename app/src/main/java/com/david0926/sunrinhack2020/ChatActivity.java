package com.david0926.sunrinhack2020;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
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

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import gun0912.tedimagepicker.builder.TedImagePicker;
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
        binding.setImageUri("");
        binding.setIsChatting(false);

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

        binding.btnChatQ1.setOnClickListener(view -> aiSays("오늘 있었던 일 중, 가장 기억에 남는 일은 무엇인가요?"));
        binding.btnChatQ2.setOnClickListener(view -> aiSays("오늘 먹었던 음식 중, 기억에 남는 음식을 하나 알려주세요."));
        binding.btnChatQ3.setOnClickListener(view -> aiSays("오늘 있었던 일 중, 당신의 기분을 좋게 만들었던 일은 무엇인가요?"));
        binding.btnChatQ4.setOnClickListener(view -> aiSays("일기에 더 남기고 싶은 내용이 있다면, 자유롭게 들려주세요!"));

        binding.btnChatSend.setOnClickListener(view -> {
            if (TextUtils.isEmpty(binding.edtChatMessage.getText())) {
                Toast.makeText(this, "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> map = new HashMap<>();
            map.put("author", UserCache.getUser(this).getName());
            map.put("text", binding.getMessage());
            map.put("time", timeNow());
            map.put("image", binding.getImageUri().isEmpty()?"":binding.getImageUri());


            firebaseFirestore
                    .collection("users")
                    .document(UserCache.getUser(this).getEmail())
                    .update("chat", FieldValue.arrayUnion(map))
                    .addOnSuccessListener(runnable -> {
                        binding.setIsChatting(false);
                        Gson gson = new Gson();
                        JsonElement e = gson.toJsonTree(map);
                        ChatModel model = gson.fromJson(e, ChatModel.class);
                        chatItems.add(model);
                        binding.setMessage("");
                        binding.horizontalScrollView.setVisibility(View.VISIBLE);
                        binding.recyclerChat.smoothScrollToPosition(chatItems.size()-1);
                    });
        });

        binding.btnChatImage.setOnClickListener(view -> {
            //start image picker
            TedImagePicker
                    .with(this)
                    .showTitle(false)
                    .startAnimation(R.anim.slide_up, R.anim.slide_up_before)
                    .finishAnimation(R.anim.slide_down_before, R.anim.slide_down)
                    .start(uri -> {
                        if (getMimeType(uri).equals("image/jpeg") || getMimeType(uri).equals("image/png")) {
                            binding.setImageUri(uri.toString());
                        } else Toast.makeText(this, "Please upload valid profile image. (jpeg, png)", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void aiSays(String text){
        Map<String, Object> map = new HashMap<>();
        map.put("author", "AI");
        map.put("text", text);
        map.put("time", timeNow());

        firebaseFirestore
                .collection("users")
                .document(UserCache.getUser(this).getEmail())
                .update("chat", FieldValue.arrayUnion(map))
                .addOnSuccessListener(runnable -> {
                    binding.setIsChatting(true);
                    Gson gson = new Gson();
                    JsonElement e = gson.toJsonTree(map);
                    ChatModel model = gson.fromJson(e, ChatModel.class);
                    chatItems.add(model);
                    binding.recyclerChat.smoothScrollToPosition(chatItems.size()-1);
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
                    binding.recyclerChat.smoothScrollToPosition(chatItems.size()-1);
                    binding.setIsChatting(chatItems.get(chatItems.size()-1).getAuthor().equals("AI"));
                    //Collections.reverse(chatItems);
                });
    }

    private String timeNow() {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ENGLISH).format(new Date());
    }

    public String getMimeType(Uri uri) {

        String mimeType;
        if (uri.getScheme() != null && uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = this.getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private byte[] imageToByte(Drawable drawable) {

        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 10, outputStream);

        return outputStream.toByteArray();
    }
}