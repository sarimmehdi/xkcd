package com.sarim.xkcd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.comic.ComicAdapter;
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
                comics -> {
                    Log.d("Sarim", "start with " + comics.size());
                    for (Comic comic : comics) {
                        Log.d("Sarim", new Gson().toJson(comic));
                    }
                    Log.d("Sarim", "end with " + comics.size());
                    comicListBinding.setComicAdapter(new ComicAdapter(comics));
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.createBackgroundThreads();

        // get first 20 comics
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