package com.sarim.xkcd;

import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sarim.xkcd.comic.Comic;
import com.sarim.xkcd.comic.ComicRepository;
import com.sarim.xkcd.retrofit.RetrofitHelper;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ViewModel extends AndroidViewModel {
    @NonNull
    private final ComicRepository comicRepository;
    private final LiveData<List<Comic>> allComics;

    // for communicating with sqlite
    private HandlerThread viewModelThread;
    private Handler viewModelHandler;

    // this will be used for retrieving comics from server
    private final RetrofitHelper retrofitHelper = new RetrofitHelper();

    // check which page the user is on when viewing all comics
    private final ObservableInt currPageAllComics = new ObservableInt(0);

    // check which page the user is on when viewing only favorite comics
    private final ObservableInt currPageFavComics = new ObservableInt(0);

    // the way comics are sorted on each page depends on whether you are in the favorites tab or not
    private final ObservableBoolean favoriteTab = new ObservableBoolean(false);

    // this is what the user will see on the edit text dialog
    public ObservableInt editTextCurrPage = new ObservableInt(
            currPageAllComics, currPageFavComics, favoriteTab
    ) {
        @Override
        public int get() {
            if (favoriteTab.get()) {
                return currPageFavComics.get();
            }
            else {
                return currPageAllComics.get();
            }
        }
    };

    // show only a certain number of comics per page
    private static final int MAX_COMICS_PER_PAGE = 5;

    public ViewModel(@NonNull Application application) {
        super(application);
        comicRepository = new ComicRepository(application);
        allComics = comicRepository.getAllComics();
    }

    public ViewModel(ComicRepository comicRepository, @NonNull Application application) {
        super(application);
        this.comicRepository = comicRepository;
        allComics = comicRepository.getAllComics();
    }

    /**
     * create background thread for doing sqlite transactions
     */
    public void createBackgroundThreads() {
        viewModelThread = new HandlerThread("viewModelThread");
        viewModelThread.start();
        viewModelHandler = new Handler(viewModelThread.getLooper());
    }

    public void getRecentlyAddedComic(Consumer<Comic> comicConsumer) {
        retrofitHelper.getRecentlyAddedComic(comicConsumer::accept);
    }

    public int getCurrPageAllComics() {
        return currPageAllComics.get();
    }

    public void setCurrPageAllComics(int currPageAllComics) {
        this.currPageAllComics.set(Math.max(0, currPageAllComics));
    }

    public int getCurrPageFavComics() {
        return currPageFavComics.get();
    }

    public void setCurrPageFavComics(int currPageFavComics) {
        this.currPageFavComics.set(Math.max(0, currPageFavComics));
    }

    public void setFavoriteTab(boolean favoriteTab) {
        this.favoriteTab.set(favoriteTab);
    }

    public boolean isFavoriteTab() {
        return favoriteTab.get();
    }

    /**
     * retrieve specific comic using just the number value and display it in a separate activity
     * @param num the number for which you wish to retrieve the comic
     * @param comicConsumer what you do with the comic after retrieving it
     */
    public void getComicFromServer(int num, Consumer<Comic> comicConsumer) {
        retrofitHelper.getComic(num, comicConsumer::accept);
    }

    /**
     * First obtain the new range of numbers from the provided page number for which you want to try
     * to grab comics from the server. If you get at least one comic in the new range, then apply
     * the page number, since each page must have at least 1 comic to view
     * @param pageNumber pass a value that will be used to find comics in a range
     */
    public void getComicsFromServer(int pageNumber) {
        int firstComicOnCurrPage = pageNumber * MAX_COMICS_PER_PAGE + 1;
        int lastComicOnCurrPage = firstComicOnCurrPage + MAX_COMICS_PER_PAGE - 1;
        for (int id = firstComicOnCurrPage; id <= lastComicOnCurrPage; id++) {
            retrofitHelper.getComic(id, comic -> {
                if (comic != null) {
                    insertComicOnDevice(comic);
                }
            });
        }
    }

    public int getMaxComicsPerPage() {
        return MAX_COMICS_PER_PAGE;
    }

    /**
     * First check whether there are comics on the remote database for the new page number before
     * trying to change the page number to the new one
     * @param pageNumToGoTo page number you want to check for
     * @param onBeingAllowedToChangePage if there is at least one comic for the new page, then
     *                                   execute this code
     */
    public void canChangeAllComicsPage(int pageNumToGoTo, Runnable onBeingAllowedToChangePage) {
        int firstComicOnSupposedNextPage = pageNumToGoTo * MAX_COMICS_PER_PAGE + 1;
        retrofitHelper.getComic(firstComicOnSupposedNextPage, comic -> {
            if (comic != null) {
                onBeingAllowedToChangePage.run();
            }
        });
    }

    /**
     * First check whether there are comics inside the Room persistent database on the phone
     * for the new page number before trying to change the page number to the new one
     * @param pageNumToGoTo page number you want to check for
     * @return true if the page can be changed, false otherwise
     */
    public boolean canChangeFavComicsPage(int pageNumToGoTo) {
        int firstComicOnSupposedNextPage = pageNumToGoTo * MAX_COMICS_PER_PAGE;
        List<Comic> comics = allComics.getValue();
        if (comics != null) {
            return firstComicOnSupposedNextPage >= 0 && firstComicOnSupposedNextPage < comics.size();
        }
        return false;
    }

    public List<Comic> getFavComicsForCurrPageOnly(List<Comic> comics) {
        int firstComicOnCurrPage = currPageFavComics.get() * MAX_COMICS_PER_PAGE + 1;
        int lastComicOnCurrPage = firstComicOnCurrPage + MAX_COMICS_PER_PAGE - 1;
        return comics.subList(
                Math.min(firstComicOnCurrPage - 1, comics.size() - 1),
                Math.min(lastComicOnCurrPage, comics.size())
        );
    }

    public List<Comic> getAllComicsForCurrPageOnly(List<Comic> comics) {
        int firstComicOnCurrPage = currPageAllComics.get() * MAX_COMICS_PER_PAGE + 1;
        int lastComicOnCurrPage = firstComicOnCurrPage + MAX_COMICS_PER_PAGE - 1;
        return comics.stream().filter(comic -> comic.getNum() >= firstComicOnCurrPage
                && comic.getNum() <= lastComicOnCurrPage).collect(Collectors.toList());
    }

    private void insertComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
    }

    public void updateComicOnDevice(Comic comic) {
        viewModelHandler.post(() -> comicRepository.update(comic));
    }

    public void deleteOnlyNonFavoriteComicsOnDevice() {
        viewModelHandler.post(comicRepository::deleteNonFavorites);
    }

    /**
     * Just update the favorites attribute for the comics stored on the device by assigning the same
     * value as before. This is just to trigger the Observer which then causes the RecyclerView to
     * be updated
     */
    public void forceRefresh() {
        List<Comic> comics = allComics.getValue();
        if (comics != null) {
            for (Comic comic : comics) {
                comic.setFavorite(comic.isFavorite());
                updateComicOnDevice(comic);
            }
        }
    }

    public void deleteComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.delete(comic));
    }

    public void insertComic(Comic comic) {
        viewModelHandler.post(() -> comicRepository.insert(comic));
    }

    public LiveData<List<Comic>> getAllComicsOnDevice() {
        return allComics;
    }

    /**
     * safely quit the view model thread
     */
    public void quitThreads() {
        viewModelThread.quitSafely();
    }
}

