package com.sarim.xkcd.ui.databinding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.sarim.xkcd.comic.Comic;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class ImgDatabinding {

    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, Comic comic) {
        if (comic.getImg().isEmpty()) {
            Picasso.get().load("https://imgs.xkcd.com/comics/form.png").into(view);
        }
        else {
            if (comic.isFavorite()) {
                Picasso.get().load(comic.getImg()).networkPolicy(NetworkPolicy.NO_STORE).into(view);
            }
            else {
                Picasso.get().load(comic.getImg()).into(view);
            }
        }
    }
}
