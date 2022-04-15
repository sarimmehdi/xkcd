package com.sarim.xkcd.comic;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Comic.class}, version = 1)
public abstract class ComicDatabase extends RoomDatabase {

    private static ComicDatabase instance;

    public abstract ComicDao comicDao();

    public static synchronized ComicDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ComicDatabase.class, "comic_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
