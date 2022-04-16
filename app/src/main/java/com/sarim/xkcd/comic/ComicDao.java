package com.sarim.xkcd.comic;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ComicDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Comic comic);

    @Update
    void update(Comic comic);

    @Delete
    void delete(Comic comic);

    @Query("DELETE FROM comic_table")
    void deleteAll();

    @Query("DELETE FROM comic_table WHERE favorite = 0")
    void deleteNonFavorites();

    @Query("SELECT * FROM comic_table")
    LiveData<List<Comic>> getAllComics();
}
