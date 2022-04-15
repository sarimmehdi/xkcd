package com.sarim.xkcd.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.sarim.xkcd.R;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;

public class ComicViewingActivity extends AppCompatActivity implements ExplanationClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ComicBinding comicBinding = DataBindingUtil.setContentView(this, R.layout.comic);
        Bundle data = getIntent().getExtras();
        Comic comic = data.getParcelable("comic");
        comicBinding.setComic(comic);
        comicBinding.executePendingBindings();
    }

    @Override
    public void explanationClicked(Comic comic) {

    }
}
