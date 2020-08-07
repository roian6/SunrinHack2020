package com.david0926.sunrinhack2020.util;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;
import androidx.databinding.BindingAdapter;
import androidx.databinding.BindingConversion;
import androidx.databinding.ObservableArrayList;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.david0926.sunrinhack2020.R;
import com.david0926.sunrinhack2020.adapter.ChatAdapter;
import com.david0926.sunrinhack2020.model.ChatModel;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BindingOptions {

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

    @BindingAdapter("imageLink")
    public static void setImageLink(ImageView view, String link) {
        if (link == null || link.isEmpty()) return;
        Glide.with(view).load(link).into(view);
    }

    @BindingAdapter("chatItem")
    public static void bindChatItem(RecyclerView recyclerView, ObservableArrayList<ChatModel> items) {
        ChatAdapter adapter = (ChatAdapter) recyclerView.getAdapter();
        if (adapter != null) adapter.setItem(items);
    }

    @BindingAdapter("buttonEnabled")
    public static void setButtonEnabled(View button, Boolean enabled) {
        button.setEnabled(enabled);
        button.setBackgroundTintList(ContextCompat.getColorStateList(button.getContext(),
                enabled ? R.color.colorPrimary : R.color.materialGray5));
    }

}
