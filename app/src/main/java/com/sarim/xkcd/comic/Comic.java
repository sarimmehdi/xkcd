package com.sarim.xkcd.comic;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "comic_table")
public class Comic implements Parcelable {

    @PrimaryKey
    private int num;

    private String month;
    private String link;
    private String year;
    private String news;

    @SerializedName("safe_title")
    private String safeTitle;

    private String transcript;
    private String alt;
    private String img;
    private String title;
    private String day;
    private boolean favorite;

    public Comic(String month, int num, String link, String year, String news, String safeTitle,
                 String transcript, String alt, String img, String title, String day,
                 boolean favorite) {
        this.month = month;
        this.num = num;
        this.link = link;
        this.year = year;
        this.news = news;
        this.safeTitle = safeTitle;
        this.transcript = transcript;
        this.alt = alt;
        this.img = img;
        this.title = title;
        this.day = day;
        this.favorite = favorite;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public Comic(Parcel in){
        month = in.readString();
        num = in.readInt();
        link = in.readString();
        year = in.readString();
        news = in.readString();
        safeTitle = in.readString();
        transcript = in.readString();
        alt = in.readString();
        img = in.readString();
        title = in.readString();
        day = in.readString();
        favorite = in.readBoolean();
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getSafeTitle() {
        return safeTitle;
    }

    public void setSafeTitle(String safeTitle) {
        this.safeTitle = safeTitle;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public static final Parcelable.Creator<Comic> CREATOR = new Parcelable.Creator<Comic>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        public Comic createFromParcel(Parcel in) {
            return new Comic(in);
        }

        public Comic[] newArray(int size) {
            return new Comic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(month);
        dest.writeInt(num);
        dest.writeString(link);
        dest.writeString(year);
        dest.writeString(news);
        dest.writeString(safeTitle);
        dest.writeString(transcript);
        dest.writeString(alt);
        dest.writeString(img);
        dest.writeString(title);
        dest.writeString(day);
        dest.writeBoolean(favorite);
    }

    @NonNull
    @Override
    public String toString() {
        return "Comic{month='" + month + '\'' +
                ", num='" + num + '\'' +
                ", link='" + link + '\'' +
                ", year='" + year + '\'' +
                ", news='" + news + '\'' +
                ", safeTitle='" + safeTitle + '\'' +
                ", transcript='" + transcript + '\'' +
                ", alt='" + alt + '\'' +
                ", img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", day='" + day + '\'' +
                ", favorite='" + favorite + '\'' +
                '}';
    }
}
