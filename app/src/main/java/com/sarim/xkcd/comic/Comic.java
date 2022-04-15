package com.sarim.xkcd.comic;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "comic_table")
public class Comic {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String month;
    private int num;
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

    public Comic(String month, int num, String link, String year, String news, String safeTitle,
                 String transcript, String alt, String img, String title, String day) {
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
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
