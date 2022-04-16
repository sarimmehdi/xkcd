package com.sarim.xkcd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.sarim.xkcd.databinding.ComicListBinding;
import com.sarim.xkcd.di.DaggerMainActivityComponents;
import com.sarim.xkcd.di.MainActivityComponents;
import com.sarim.xkcd.di.ProvidersModule;
import com.sarim.xkcd.ui.ComicAdapter;
import com.sarim.xkcd.usecases.OnAllComicsBtnClicked;
import com.sarim.xkcd.usecases.OnAppExit;
import com.sarim.xkcd.usecases.OnAppStart;
import com.sarim.xkcd.usecases.OnEditTextChanged;
import com.sarim.xkcd.usecases.OnFavBtnClicked;
import com.sarim.xkcd.usecases.OnNextPageBtnClicked;
import com.sarim.xkcd.usecases.OnPrevBtnClicked;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private ComicListBinding comicListBinding;
    private ViewModel viewModel;

    // all the different functions our app performs
    @Inject
    OnAllComicsBtnClicked onAllComicsBtnClicked;
    @Inject
    OnAppExit onAppExit;
    @Inject
    OnAppStart onAppStart;
    @Inject
    OnEditTextChanged onEditTextChanged;
    @Inject
    OnFavBtnClicked onFavBtnClicked;
    @Inject
    OnNextPageBtnClicked onNextPageBtnClicked;
    @Inject
    OnPrevBtnClicked onPrevBtnClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comicListBinding = DataBindingUtil.setContentView(this, R.layout.comic_list);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllComicsOnDevice().observe(
                this,
                comics -> {
                    comicListBinding.setComicAdapter(new ComicAdapter(comics));
                    comicListBinding.executePendingBindings();
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.createBackgroundThreads();
        comicListBinding.setViewModel(viewModel);
        MainActivityComponents mainActivityComponents = DaggerMainActivityComponents.builder()
                .providersModule(new ProvidersModule(viewModel, comicListBinding, this))
                .build();
        mainActivityComponents.inject(this);

        // this use case is executed when the app is started
        onAppStart.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // this use case is executed when you close the app
        onAppExit.execute();
    }
}