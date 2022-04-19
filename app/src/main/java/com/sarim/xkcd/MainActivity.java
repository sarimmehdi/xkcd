package com.sarim.xkcd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;

import com.sarim.xkcd.databinding.ComicListBinding;
import com.sarim.xkcd.di.DaggerMainActivityComponents;
import com.sarim.xkcd.di.MainActivityComponents;
import com.sarim.xkcd.di.MainActivityProvidersModule;
import com.sarim.xkcd.usecases.mainactivity.OnAllComicsBtnClicked;
import com.sarim.xkcd.usecases.mainactivity.OnAppExit;
import com.sarim.xkcd.usecases.mainactivity.OnAppStart;
import com.sarim.xkcd.usecases.mainactivity.OnEditTextChanged;
import com.sarim.xkcd.usecases.mainactivity.OnFavBtnClicked;
import com.sarim.xkcd.usecases.mainactivity.OnNextPageBtnClicked;
import com.sarim.xkcd.usecases.mainactivity.OnOnlineSearchRequest;
import com.sarim.xkcd.usecases.mainactivity.OnPrevBtnClicked;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    ComicListBinding comicListBinding;

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
    @Inject
    OnOnlineSearchRequest onOnlineSearchRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comicListBinding = DataBindingUtil.setContentView(this, R.layout.comic_list);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Context context = this;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // initialize view model and databinding for comic list before adding them to dependency graph
        ViewModel viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.createBackgroundThreads();
        comicListBinding.setViewModel(viewModel);
        MainActivityComponents mainActivityComponents = DaggerMainActivityComponents.builder()
                .mainActivityProvidersModule(new MainActivityProvidersModule(
                        viewModel, comicListBinding, notificationManager,
                        this,this)).build();
        mainActivityComponents.inject(this);

        // this use case is executed when the app is started
        onAppStart.execute();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            WindowInsetsControllerCompat windowInsetsController =
                    ViewCompat.getWindowInsetsController(this.getWindow().getDecorView());
            if (windowInsetsController == null) {
                return;
            }
            // Configure the behavior of the hidden system bars
            windowInsetsController.setSystemBarsBehavior(
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            );
            // Hide both the status bar and the navigation bar
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // this use case is executed when you close the app
        onAppExit.execute();
    }
}