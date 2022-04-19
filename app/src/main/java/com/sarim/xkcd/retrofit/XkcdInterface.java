package com.sarim.xkcd.retrofit;

import com.sarim.xkcd.comic.Comic;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface XkcdInterface {

    /**
     * query the xkcd api to get the latest comic
     * @return Comic object
     */
    @GET("info.0.json")
    Call<Comic> getRecentlyAddedComic();

    /**
     * query the xkcd api to get the comic based on id (comic.getNum())
     * @param id the id of the comic you wish to retrieve from the remote database
     * @return Comic object
     */
    @GET("{id}/info.0.json")
    Call<Comic> getComic(@Path("id") int id);
}
