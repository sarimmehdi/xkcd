package com.sarim.xkcd.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sarim.xkcd.R;
import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;
import com.sarim.xkcd.ui.interfaces.ExplanationClickListener;
import com.sarim.xkcd.ui.interfaces.OnFavComicClickListener;
import com.sarim.xkcd.usecases.OnFavStarBtnClicked;

import javax.inject.Inject;

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
