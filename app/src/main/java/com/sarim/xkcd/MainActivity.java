package com.sarim.xkcd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.sarim.xkcd.databinding.ComicListBinding;
import com.sarim.xkcd.ui.AllComicsClickListener;
import com.sarim.xkcd.ui.ComicAdapter;
import com.sarim.xkcd.ui.FavoritesClickListener;
import com.sarim.xkcd.ui.NextPageClickListener;
import com.sarim.xkcd.ui.PrevPageClickListener;

public class MainActivity extends AppCompatActivity
        implements AllComicsClickListener, FavoritesClickListener,
        NextPageClickListener, PrevPageClickListener {

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
                    comicListBinding.setComicAdapter(new ComicAdapter(comics));
                    comicListBinding.executePendingBindings();
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.createBackgroundThreads();
        viewModel.getComicsFromServer();
        comicListBinding.setViewModel(viewModel);
        comicListBinding.setAllComicsClickListener(this);
        comicListBinding.setFavoritesClickListener(this);
        comicListBinding.setNextPageClickListener(this);
        comicListBinding.setPrevPageClickListener(this);
        comicListBinding.favoritesTab.setBackgroundColor(
                ContextCompat.getColor(this, R.color.white)
        );
        comicListBinding.allComicsTab.setBackgroundColor(
                ContextCompat.getColor(this, R.color.silver)
        );

        // listen for edit text changes
        comicListBinding.editTextPageNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int pageNum = Integer.parseInt(editable.toString());
                    viewModel.deleteAllComicsOnDevice();
                    viewModel.setCurrPage(pageNum);
                    viewModel.getComicsFromServer();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.deleteAllComicsOnDevice();
        viewModel.quitThreads();
    }

    @Override
    public void allComicsButtonClicked() {
        viewModel.deleteAllComicsOnDevice();
        viewModel.setFavoritesTab(false);
        viewModel.getComicsFromServer();
        comicListBinding.favoritesTab.setBackgroundColor(
                ContextCompat.getColor(this, R.color.white)
        );
        comicListBinding.allComicsTab.setBackgroundColor(
                ContextCompat.getColor(this, R.color.silver)
        );
    }

    @Override
    public void favButtonClicked() {
        viewModel.deleteAllComicsOnDevice();
        viewModel.setFavoritesTab(true);
        viewModel.getComicsFromServer();
        comicListBinding.favoritesTab.setBackgroundColor(
                ContextCompat.getColor(this, R.color.silver)
        );
        comicListBinding.allComicsTab.setBackgroundColor(
                ContextCompat.getColor(this, R.color.white)
        );
    }

    @Override
    public void nextPageBtnClicked() {
        viewModel.deleteAllComicsOnDevice();
        viewModel.incCurrPage();
        viewModel.getComicsFromServer();
    }

    @Override
    public void prevPageBtnClicked() {
        viewModel.deleteAllComicsOnDevice();
        viewModel.decCurrPage();
        viewModel.getComicsFromServer();
    }
}