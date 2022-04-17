package com.sarim.xkcd.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.sarim.xkcd.R;
import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;
import com.sarim.xkcd.di.ComicViewingActivityComponents;
import com.sarim.xkcd.di.ComicViewingActivityProvidersModule;
import com.sarim.xkcd.di.DaggerComicViewingActivityComponents;
import com.sarim.xkcd.usecases.comicviewingactivity.OnComicViewingActivityEnd;
import com.sarim.xkcd.usecases.comicviewingactivity.OnComicViewingActivityStart;
import com.sarim.xkcd.usecases.comicviewingactivity.OnExplanationBtnClicked;
import com.sarim.xkcd.usecases.comicviewingactivity.OnFavStarBtnClickedWhileViewingComic;
import com.sarim.xkcd.usecases.comicviewingactivity.OnSendComicViaEmail;

import javax.inject.Inject;

public class ComicViewingActivity extends AppCompatActivity {

    private ComicBinding comicBinding;

    // all the use cases for this activity
    @Inject
    OnComicViewingActivityEnd onComicViewingActivityEnd;
    @Inject
    OnComicViewingActivityStart onComicViewingActivityStart;
    @Inject
    OnExplanationBtnClicked onExplanationBtnClicked;
    @Inject
    OnFavStarBtnClickedWhileViewingComic onFavStarBtnClickedWhileViewingComic;
    @Inject
    OnSendComicViaEmail onSendComicViaEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comicBinding = DataBindingUtil.setContentView(this, R.layout.comic);
    }

    @Override
    protected void onStart() {
        super.onStart();

        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.createBackgroundThreads();
        Bundle data = getIntent().getExtras();
        Comic comic = data.getParcelable("comic");
        ComicViewingActivityComponents comicViewingActivityComponents = DaggerComicViewingActivityComponents.builder()
                .comicViewingActivityProvidersModule(new ComicViewingActivityProvidersModule(
                        viewModel, comicBinding, comic, this)).build();
        comicViewingActivityComponents.inject(this);
        onComicViewingActivityStart.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // this use case is executed when you close this activity
        onComicViewingActivityEnd.execute();
    }
}
