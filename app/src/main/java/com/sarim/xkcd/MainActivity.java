package com.sarim.xkcd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.sarim.xkcd.ui.ComicAdapter;
import com.sarim.xkcd.databinding.ComicListBinding;

public class MainActivity extends AppCompatActivity {

    private ComicListBinding comicListBinding;
    private ViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        comicListBinding = DataBindingUtil.setContentView(this, R.layout.comic_list);

        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllComicsOnDevice().observe(
                this,
                comics -> comicListBinding.setComicAdapter(new ComicAdapter(comics))
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.createBackgroundThreads();

        // get first 5 comics
        for (int id = 1; id <= 2; id++) {
            viewModel.getComicFromServer(id);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.deleteAllComicsOnDevice();
        viewModel.quitThreads();
    }
}