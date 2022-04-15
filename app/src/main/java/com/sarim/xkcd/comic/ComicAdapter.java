package com.sarim.xkcd.comic;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sarim.xkcd.BR;
import com.sarim.xkcd.R;
import com.sarim.xkcd.databinding.ComicSummaryBinding;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder>
        implements ComicClickListener {

    private final List<Comic> comics;

    public ComicAdapter(List<Comic> comics) {
        this.comics = comics;
    }

    @NonNull
    @Override
    public ComicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ComicSummaryBinding comicSummaryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.comic_summary, parent, false
        );
        return new ViewHolder(comicSummaryBinding);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comic comic = comics.get(position);
        holder.bind(comic);
        holder.comicSummaryBinding.setComicClickListener(this);
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    @Override
    public void comicClicked(Comic comic) {

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ComicSummaryBinding comicSummaryBinding;

        public ViewHolder(ComicSummaryBinding comicSummaryBinding) {
            super(comicSummaryBinding.getRoot());
            this.comicSummaryBinding = comicSummaryBinding;
        }

        public void bind(Object obj) {
            comicSummaryBinding.setVariable(BR.comic, obj);
            comicSummaryBinding.executePendingBindings();
        }
    }
}
