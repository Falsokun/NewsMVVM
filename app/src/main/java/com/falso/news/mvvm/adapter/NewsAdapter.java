package com.falso.news.mvvm.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.falso.news.mvvm.R;
import com.falso.news.mvvm.databinding.ItemNewsBinding;
import com.falso.news.mvvm.pojo.News;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsHolder> {

    private ArrayList<News> data;

    public NewsAdapter(@NonNull ArrayList<News> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NewsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsHolder(ItemNewsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsHolder holder, int position) {
        News news = data.get(holder.getLayoutPosition());
        holder.binding.setNews(news);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(16));
        Glide.with(holder.binding.getRoot().getContext())
                .load(news.getImgUrl())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.mipmap.ic_launcher)
                .into(holder.binding.img);

        if (holder.expanded) {
            holder.binding.description.setMaxLines(Integer.MAX_VALUE);
        } else {
            holder.binding.description.setMaxLines(2);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<News> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        ItemNewsBinding binding;

        boolean expanded = false;

        NewsHolder(@NonNull ItemNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            this.binding.getRoot().setOnClickListener(v -> {
                expanded = !expanded;
            });
        }
    }
}
