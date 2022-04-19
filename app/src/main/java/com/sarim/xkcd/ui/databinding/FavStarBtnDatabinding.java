package com.sarim.xkcd.ui.databinding;

import android.widget.ImageButton;

import androidx.databinding.BindingAdapter;

public class FavStarBtnDatabinding {

    /**
     * highlights the star as red if the associated comic object is favorite or not
     * @param imageButton the ImageButton object that will changed to red or silver
     * @param favorite the favorite status of the comic
     */
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
