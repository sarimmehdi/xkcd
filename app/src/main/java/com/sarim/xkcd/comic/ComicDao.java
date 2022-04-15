package com.sarim.xkcd.comic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ComicDao {

    @Insert
    void insert(Comic comic);

    @Update
    void update(Comic comic);

    @Delete
    void delete(Comic comic);

    @Query("DELETE FROM comic_table")
    void deleteAll();

    @Query("SELECT * FROM comic_table")
    LiveData<List<Comic>> getAllComics();
}
