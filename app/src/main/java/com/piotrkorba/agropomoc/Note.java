package com.piotrkorba.agropomoc;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * Entity class that represents single note in the database.
 */
@Entity(tableName = "notes_table")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private Date date;
    private long currencyInteger;
    private long currenctDecimal;
    private String title;
    private String content;
    private String imageUri;

    public Note(@NonNull Date date, long currencyInteger, long currenctDecimal, String title, String content, String imageUri) {
        this.date = date;
        this.currencyInteger = currencyInteger;
        this.currenctDecimal = currenctDecimal;
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }

    public long getCurrencyInteger() {
        return currencyInteger;
    }

    public void setCurrencyInteger(long currencyInteger) {
        this.currencyInteger = currencyInteger;
    }

    public long getCurrenctDecimal() {
        return currenctDecimal;
    }

    public void setCurrenctDecimal(long currenctDecimal) {
        this.currenctDecimal = currenctDecimal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
