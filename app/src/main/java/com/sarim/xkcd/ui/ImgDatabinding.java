package com.sarim.xkcd.ui;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;

public class ImgDatabinding {

    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, String imageUrl) {
        Picasso.get().load(imageUrl).into(view);
    }
}
