package com.sarim.xkcd.ui.databinding;

import android.widget.ImageButton;

import androidx.databinding.BindingAdapter;

public class FavStarBtnDatabinding {

    @BindingAdapter({"favorite"})
    public static void setFavorite(ImageButton imageButton, boolean favorite) {
        if (favorite) {
            imageButton.setImageResource(android.R.drawable.star_big_on);
        }
        else {
            imageButton.setImageResource(android.R.drawable.star_big_off);
        }
    }
}
