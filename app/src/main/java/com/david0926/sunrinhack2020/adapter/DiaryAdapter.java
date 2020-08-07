package com.david0926.sunrinhack2020.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.david0926.sunrinhack2020.databinding.RowDiaryBinding;
import com.david0926.sunrinhack2020.model.DiaryModel;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryHolder> {

    private List<DiaryModel> list;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, DiaryModel item);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, DiaryModel item);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    public DiaryAdapter() {
        this.list = new ArrayList<>();
    }

    @NonNull
    @Override
    public DiaryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiaryHolder(RowDiaryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryHolder holder, int position) {
        DiaryModel item = list.get(position);
        holder.bind(item, mOnItemClickListener, mOnItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setItem(List<DiaryModel> items) {
        this.list = items;
        notifyDataSetChanged();
    }

    static class DiaryHolder extends RecyclerView.ViewHolder {

        private RowDiaryBinding binding;

        DiaryHolder(RowDiaryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(DiaryModel item, OnItemClickListener listener, OnItemLongClickListener longListener) {
            binding.setItem(item);
            itemView.setOnClickListener(view -> listener.onItemClick(view, item));
            itemView.setOnLongClickListener(view -> longListener.onItemLongClick(view, item));
        }
    }
}

