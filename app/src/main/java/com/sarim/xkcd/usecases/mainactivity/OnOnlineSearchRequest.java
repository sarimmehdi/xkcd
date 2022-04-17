package com.sarim.xkcd.usecases.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.databinding.ComicListBinding;
import com.sarim.xkcd.ui.ComicViewingActivity;
import com.sarim.xkcd.ui.SearchWebViewActivity;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnOnlineSearchRequest {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final Context context;

    @Inject
    public OnOnlineSearchRequest(ViewModel viewModel, ComicListBinding comicListBinding,
                                 Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.context = context;
    }

    @Inject
    public void execute() {
        comicListBinding.comicOnlineSearch.setOnKeyListener((view, i, keyEvent) -> {
            if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                String inputByUser = comicListBinding.comicOnlineSearch.getText().toString();
                try {
                    // if it is just a digit, load the comic
                    int comicNum = Integer.parseInt(inputByUser);
                    viewModel.getComicFromServer(comicNum, comic -> {
                        if (comic != null) {
                            Intent intent = new Intent(context, ComicViewingActivity.class);
                            intent.putExtra("comic", comic);
                            context.startActivity(intent);
                        }
                    });
                } catch (NumberFormatException e) {
                    // else open a webview where you search with whatever text was provided
                    Intent intent = new Intent(context, SearchWebViewActivity.class);
                    intent.putExtra("search", inputByUser);
                    context.startActivity(intent);
                }
                return true;
            }
            return false;
        });
    }
}
