package com.sarim.xkcd.ui.databinding;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class ImgDatabinding {

    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, String imageUrl) {
        if (imageUrl.isEmpty()) {
            Picasso.get().load("https://imgs.xkcd.com/comics/form.png").into(view);
        }
        else {
            Picasso.get().load(imageUrl).into(view);
        }
    }
}
