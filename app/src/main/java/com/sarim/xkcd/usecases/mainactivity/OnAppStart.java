package com.sarim.xkcd.usecases.mainactivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.sarim.xkcd.R;
import com.sarim.xkcd.ViewModel;
import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.databinding.ComicListBinding;
import com.sarim.xkcd.ui.ComicAdapter;
import com.sarim.xkcd.ui.ComicViewingActivity;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OnAppStart {

    private final ViewModel viewModel;
    private final ComicListBinding comicListBinding;
    private final NotificationManagerCompat notificationManager;
    private final LifecycleOwner lifecycleOwner;
    private final Context context;

    @Inject
    public OnAppStart(ViewModel viewModel, ComicListBinding comicListBinding,
                      NotificationManagerCompat notificationManager, LifecycleOwner lifecycleOwner,
                      Context context) {
        this.viewModel = viewModel;
        this.comicListBinding = comicListBinding;
        this.notificationManager = notificationManager;
        this.lifecycleOwner = lifecycleOwner;
        this.context = context;
    }

    public void execute() {
        viewModel.setFavoriteTab(false);
        comicListBinding.comicRetrievalProgressBar.setVisibility(View.VISIBLE);
        viewModel.getComicsFromServer(viewModel.getCurrPageAllComics());
        comicListBinding.favoritesTab.setBackgroundColor(
                ContextCompat.getColor(context, R.color.white)
        );
        comicListBinding.allComicsTab.setBackgroundColor(
                ContextCompat.getColor(context, R.color.silver)
        );
        viewModel.getAllComicsOnDevice().observe(
                lifecycleOwner,
                comics -> {
                    comicListBinding.comicRetrievalProgressBar.setVisibility(View.INVISIBLE);
                    comics.forEach(comic ->
                            comic.setTranscript(comic.getTranscript().replaceAll(
                                    "[\\[\\](){}]",""
                            )));
                    List<Comic> comicsOnCurrPage;
                    if (viewModel.isFavoriteTab()) {
                        comicsOnCurrPage = viewModel.getFavComicsForCurrPageOnly(comics);
                    }
                    else {
                        comicsOnCurrPage = viewModel.getAllComicsForCurrPageOnly(comics);
                    }
                    comicListBinding.setComicAdapter(
                            new ComicAdapter(comicsOnCurrPage, comic -> {
                                comic.setFavorite(!comic.isFavorite());
                                Picasso.get().load(comic.getImg());
                                viewModel.updateComicOnDevice(comic);
                            })
                    );
                    comicListBinding.executePendingBindings();
                }
        );
        viewModel.getRecentlyAddedComic(comic -> {
            if (comic == null) {
                return;
            }
            SharedPreferences storedPreferences = context.getSharedPreferences(
                    "latestComicPref", Context.MODE_PRIVATE
            );
            String latestComicNum = storedPreferences.getString("num", "1");
            try {
                int storedNumForLatestComic = Integer.parseInt(latestComicNum);
                int latestComicNumOnServer = comic.getNum();
                if (latestComicNumOnServer > storedNumForLatestComic) {
                    SharedPreferences retrievedPreferences = context.getSharedPreferences(
                            "latestComicPref", Context.MODE_PRIVATE
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
        comicListBinding.executePendingBindings();
    }

    private void showNotification(String notification, Comic comic) {
        Intent resultIntent = new Intent(context, ComicViewingActivity.class);
        resultIntent.putExtra("comic", comic);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
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
