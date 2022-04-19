package com.sarim.xkcd.ui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sarim.xkcd.BR;
import com.sarim.xkcd.R;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicSummaryBinding;
import com.sarim.xkcd.ui.interfaces.ComicClickListener;
import com.sarim.xkcd.ui.interfaces.OnFavComicClickListener;

import java.util.List;
import java.util.function.Consumer;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ViewHolder>
        implements ComicClickListener, OnFavComicClickListener {

    private final List<Comic> comics;
    private Context context;
    private final Consumer<Comic> comicConsumerWhenFavBtnClicked;

    /**
     *
     * @param comics list of comics to be displayed in the RecyclerView
     * @param comicConsumerWhenFavBtnClicked perform an action for a comic whose favorite button is
     *                                       clicked
     */
    public ComicAdapter(List<Comic> comics, Consumer<Comic> comicConsumerWhenFavBtnClicked) {
        this.comics = comics;
        this.comicConsumerWhenFavBtnClicked = comicConsumerWhenFavBtnClicked;
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
        context = holder.itemView.getContext();
        holder.comicSummaryBinding.setComicClickListener(this);
        holder.comicSummaryBinding.setFavComicClickListener(this);
    }

    @Override
    public int getItemCount() {
        return comics.size();
    }

    /**
     * view the full comic when it is clicked in the RecyclerView
     * @param comic the Comic object for whihc we wish to look at the details
     */
    @Override
    public void comicClicked(Comic comic) {
        Intent intent = new Intent(context, ComicViewingActivity.class);
        intent.putExtra("comic", comic);
        context.startActivity(intent);
    }

    /**
     * do something with the comic if its favorite button is clicked
     * @param comic the Comic object for which the favorite button is clicked
     */
    @Override
    public void favStarButtonClicked(Comic comic) {
        comicConsumerWhenFavBtnClicked.accept(comic);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ComicSummaryBinding comicSummaryBinding;

        public ViewHolder(ComicSummaryBinding comicSummaryBinding) {
            super(comicSummaryBinding.getRoot());
            this.comicSummaryBinding = comicSummaryBinding;
        }

        /**
         * set the Comic object and refresh the RecyclerView
         * @param obj we want this to work for any generic object and not just a Comic object
         */
        public void bind(Object obj) {
            comicSummaryBinding.setVariable(BR.comic, obj);
            comicSummaryBinding.executePendingBindings();
        }
    }
}
