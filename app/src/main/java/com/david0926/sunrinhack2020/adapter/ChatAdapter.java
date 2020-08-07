package com.david0926.sunrinhack2020.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.david0926.sunrinhack2020.databinding.RowChatReceiveBinding;
import com.david0926.sunrinhack2020.databinding.RowChatSendBinding;
import com.david0926.sunrinhack2020.model.ChatModel;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {

    private List<ChatModel> list;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, ChatModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, ChatModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public ChatAdapter() {
        this.list = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getAuthor().equals("AI")) return 0;
        else return 1;
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new ChatHolder(RowChatReceiveBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        else
            return new ChatHolder(RowChatSendBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        ChatModel item = list.get(position);
        holder.bind(item, mOnItemClickListener, mOnItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setItem(List<ChatModel> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    static class ChatHolder extends RecyclerView.ViewHolder {

        private RowChatReceiveBinding receiveBinding;
        private RowChatSendBinding sendBinding;

        private String type;

        ChatHolder(RowChatReceiveBinding binding) {
            super(binding.getRoot());
            this.receiveBinding = binding;
            type="receive";
        }

        ChatHolder(RowChatSendBinding binding) {
            super(binding.getRoot());
            this.sendBinding = binding;
            type="send";
        }

        void bind(ChatModel item, OnItemClickListener listener, OnItemLongClickListener longListener) {
            if (type.equals("receive"))
                receiveBinding.setItem(item);
            else sendBinding.setItem(item);

            itemView.setOnClickListener(view -> listener.onItemClick(view, item));
            itemView.setOnLongClickListener(view -> longListener.onItemLongClick(view, item));
        }
    }
}

