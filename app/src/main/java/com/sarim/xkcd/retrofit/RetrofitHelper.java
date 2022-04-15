package com.sarim.xkcd.retrofit;

import android.util.Log;
import androidx.core.util.Consumer;
import com.sarim.xkcd.comic.Comic;

import org.jetbrains.annotations.NotNull;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * for communicating with the server
 * @author Sarim Mehdi
 */
public class RetrofitHelper {

    private static final String TAG = "RetrofitHelper";
    // for communicating with xkcd api
    private final XkcdInterface xkcdInterface;

    public RetrofitHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(30, TimeUnit.SECONDS);
        String url = "https://xkcd.com";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        xkcdInterface = retrofit.create(XkcdInterface.class);
    }

    /**
     *
     * @param id id of xkcd comic you wish to retrieve
     */
    public void getComic(int id, Consumer<Comic> comicConsumer) {
        Call<Comic> call = xkcdInterface.getComic(id);
        call.enqueue(new Callback<Comic>() {
            @Override
            public void onResponse(@NotNull Call<Comic> call,
                                   @NotNull Response<Comic> response) {
                Log.d(TAG, "response code for getComic: " + response.code());
                Log.d(TAG, "response message for getComic: " + response.message());
                if (response.isSuccessful()) {
                    Comic comic = response.body();
                    comicConsumer.accept(comic);
                }
                else {
                    comicConsumer.accept(null);
                }
            }
            @Override
            public void onFailure(@NotNull Call<Comic> call,
                                  @NotNull Throwable t) {
                Log.e(TAG, "stack trace for not being able to get comic");
                t.printStackTrace();
                comicConsumer.accept(null);
            }
        });
    }
}

