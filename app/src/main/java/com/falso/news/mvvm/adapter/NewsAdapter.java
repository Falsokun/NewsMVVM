package com.falso.news.mvvm.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.falso.news.mvvm.R;
import com.falso.news.mvvm.databinding.ItemNewsBinding;
import com.falso.news.mvvm.pojo.News;
import com.falso.news.mvvm.repository.DataRepository;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<News> data;

    private boolean isLastLoadingItem = true;

    private DataRepository.Executor<String> onClickListener;

    private final int ITEM_VIEW_TYPE_BASIC = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private final int ITEM_VIEW_TYPE_FOOTER = 1;

    public NewsAdapter(@NonNull ArrayList<News> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return new NewsHolder(
                    ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                            false));
        } else {
            return new ProgressViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_progress, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder vholder, int position) {
        if (getItemViewType(vholder.getAdapterPosition()) == ITEM_VIEW_TYPE_BASIC) {
            News news = data.get(vholder.getLayoutPosition());
            NewsHolder holder = (NewsHolder) vholder;
            holder.binding.setNews(news);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
            Glide.with(holder.binding.getRoot().getContext())
                    .load(news.getImgUrl())
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_image_broken)
                    .placeholder(R.drawable.ic_image_loading)
                    .into(holder.binding.img);

            holder.itemView.setOnClickListener(v -> {
                boolean expanded = news.isExpanded();
                news.setExpanded(!expanded);
                notifyItemChanged(position);
            });

            holder.binding.link.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onResult(news.getUrl());
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return !isLastLoadingItem || position < data.size() ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    @Override
    public int getItemCount() {
        return isLastLoadingItem ? data.size() + 1 : data.size();
    }

    public void setData(ArrayList<News> data) {
        disableLoading();
        ArrayList<News> tempData = getData();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
                new NewsAdapterDiffUtil(tempData, data));
        this.data.clear();
        this.data.addAll(data);
        diffResult.dispatchUpdatesTo(this);
        if (this.data.size() != 0) {
            enableLoading();
        }
    }

    public ArrayList<News> getData() {
        return data;
    }

    public void setOnClickListener(DataRepository.Executor<String> onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void disableLoading() {
        if (isLastLoadingItem) {
            isLastLoadingItem = false;
            notifyItemRemoved(data.size() + 1);
        }
    }

    private void enableLoading() {
        if (!isLastLoadingItem) {
            isLastLoadingItem = true;
            notifyItemInserted(data.size() + 1);
        }
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        ItemNewsBinding binding;

        NewsHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        ProgressViewHolder(View v) {
            super(v);
            progressBar = v.findViewById(R.id.progress);
        }
    }
}
