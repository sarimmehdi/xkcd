package com.sarim.xkcd.retrofit;

import com.sarim.xkcd.comic.Comic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface XkcdInterface {

    @GET("{id}/info.0.json")
    Call<Comic> getComic(@Path("id") int id);
}
