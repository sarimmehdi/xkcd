package com.sarim.xkcd.usecases.comicviewingactivity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;

import com.sarim.xkcd.R;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicBinding;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnSendComicViaEmail extends AppCompatActivity {

    private final ComicBinding comicBinding;
    private final Comic comic;
    private final Context context;

    @Inject
    public OnSendComicViaEmail(ComicBinding comicBinding, Comic comic, Context context) {
        this.comicBinding = comicBinding;
        this.comic = comic;
        this.context = context;
    }

    @Inject
    public void execute() {
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
}
