package com.sarim.xkcd.ui;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

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
import com.squareup.picasso.Picasso;

public class ComicViewingActivity extends AppCompatActivity
        implements ExplanationClickListener, OnFavComicClickListener {

    private ComicBinding comicBinding;
    private Context context;
    private ViewModel viewModel;
    private Comic comic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        comicBinding = DataBindingUtil.setContentView(this, R.layout.comic);
    }

    @Override
    protected void onStart() {
        super.onStart();

        context = this;
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllComicsOnDevice().observe(
                this,
                comics -> {
                    for (Comic comic : comics) {
                        Log.d("sarim", "the comic is now " + comic.isFavorite());
                    }
                }
        );
        viewModel.createBackgroundThreads();
        Bundle data = getIntent().getExtras();
        comic = data.getParcelable("comic");
        comicBinding.setComic(comic);
        comicBinding.setOnExplanationClicked(this);
        comicBinding.executePendingBindings();
        comicBinding.setFavComicClickListener(this);
        comicBinding.receipentEmailAddress.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                String inputByUser = comicBinding.receipentEmailAddress.getText().toString();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(inputByUser).matches()) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("message/rfc822");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{inputByUser});
                    intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.email_subject));
                    String emailBody = context.getString(R.string.comic_title) + ": " + comic.getTitle() + "\n"
                            + context.getString(R.string.comic_issue) + ": " + comic.getNum() + "\n"
                            + context.getString(R.string.comic_release) + ": " + comic.getYear() + "\n"
                            + context.getString(R.string.comic_month) + ": " + comic.getMonth() + "\n"
                            + context.getString(R.string.comic_day) + ": " + comic.getDay() + "\n"
                            + context.getString(R.string.comic_transcript) + ": " + comic.getTranscript() + "\n"
                            + comic.getImg();
                    intent.putExtra(Intent.EXTRA_TEXT, emailBody);
                    try {
                        startActivity(Intent.createChooser(
                                intent, context.getString(R.string.email_send_title, inputByUser)
                        ));
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
            return false;
        });
    }

    @Override
    public void explanationClicked(Comic comic) {
        Intent intent = new Intent(context, ExplanationWebViewActivity.class);
        intent.putExtra("comic", comic);
        context.startActivity(intent);
    }

    @Override
    public void favStarButtonClicked(Comic comic) {
        comic.setFavorite(!comic.isFavorite());
        comicBinding.setComic(comic);
        comicBinding.executePendingBindings();
        Picasso.get().load(comic.getImg());

        // comic that was set to bein
        if (comic.isFavorite()) {
            viewModel.insertComic(comic);
        }
        else {
            viewModel.deleteComic(comic);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // this use case is executed when you close the app
        viewModel.deleteOnlyNonFavoriteComicsOnDevice();
        viewModel.quitThreads();
    }
}
