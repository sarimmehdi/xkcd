package com.sarim.xkcd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicListBinding;
import com.sarim.xkcd.di.DaggerMainActivityComponents;
import com.sarim.xkcd.di.MainActivityComponents;
import com.sarim.xkcd.di.ProvidersModule;
import com.sarim.xkcd.ui.ComicAdapter;
import com.sarim.xkcd.ui.ComicViewingActivity;
import com.sarim.xkcd.usecases.OnAllComicsBtnClicked;
import com.sarim.xkcd.usecases.OnAppExit;
import com.sarim.xkcd.usecases.OnAppStart;
import com.sarim.xkcd.usecases.OnEditTextChanged;
import com.sarim.xkcd.usecases.OnFavBtnClicked;
import com.sarim.xkcd.usecases.OnFavStarBtnClicked;
import com.sarim.xkcd.usecases.OnNextPageBtnClicked;
import com.sarim.xkcd.usecases.OnOnlineSearchRequest;
import com.sarim.xkcd.usecases.OnPrevBtnClicked;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private ComicListBinding comicListBinding;
    private ViewModel viewModel;
    private Context context;
    private NotificationManagerCompat notificationManager;

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
    OnFavStarBtnClicked onFavStarBtnClicked;
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
        context = this;
        notificationManager = NotificationManagerCompat.from(context);

        // initialize view model and databinding for comic list before adding them to dependency graph
        viewModel = new ViewModelProvider(this).get(ViewModel.class);
        viewModel.getAllComicsOnDevice().observe(
                this,
                comics -> {
                    comics.forEach(comic ->
                            comic.setTranscript(comic.getTranscript().replaceAll(
                                    "[\\[\\](){}]",""
                            )));
                    List<Comic> comicsOnCurrPage = viewModel.getComicsForCurrPageOnly(comics);
                    comicListBinding.setComicAdapter(
                            new ComicAdapter(comicsOnCurrPage, onFavStarBtnClicked.execute())
                    );
                    comicListBinding.executePendingBindings();
                }
        );
        viewModel.createBackgroundThreads();
        viewModel.getRecentlyAddedComic(comic -> {
            SharedPreferences storedPreferences = getSharedPreferences(
                    "latestComicPref", MODE_PRIVATE
            );
            String latestComicNum = storedPreferences.getString("num", "1");
            try {
                int storedNumForLatestComic = Integer.parseInt(latestComicNum);
                int latestComicNumOnServer = comic.getNum();
                if (latestComicNumOnServer > storedNumForLatestComic) {
                    SharedPreferences retrievedPreferences = getSharedPreferences(
                            "latestComicPref", MODE_PRIVATE
                    );
                    SharedPreferences.Editor editor = retrievedPreferences.edit();
                    editor.putString("num", String.valueOf(latestComicNumOnServer));
                    editor.apply();
                    showNotification(
                            context.getString(
                                    R.string.notification_new_comic_msg,
                                    String.valueOf(latestComicNumOnServer)
                            ),
                            comic
                    );
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        });
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

    private void showNotification(String notification, Comic comic) {
        Intent resultIntent = new Intent(context, ComicViewingActivity.class);
        resultIntent.putExtra("comic", comic);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context,
                        context.getString(R.string.notification_new_comic_id))
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText(notification)
                        .setChannelId(context.getString(R.string.notification_new_comic_id))
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(notification))
                        .setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        long time = new Date().getTime();
        String tmpStr = String.valueOf(time);
        String last4Str = tmpStr.substring(tmpStr.length() - 5);
        int notificationId = Integer.parseInt(last4Str);
        notificationManager.notify(notificationId, builder.build());
    }
}