package com.piotrkorba.agropomoc;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
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
    private long currencyDecimal;
    private String title;
    private String content;
    private String imageUri;

    public Note(@NonNull Date date, long currencyInteger, long currencyDecimal, String title, String content, String imageUri) {
        this.date = date;
        this.currencyInteger = currencyInteger;
        this.currencyDecimal = currencyDecimal;
        this.title = title;
        this.content = content;
        this.imageUri = imageUri;
    }

    @Ignore
    public Note(int id, @NonNull Date date, long currencyInteger, long currencyDecimal, String title, String content, String imageUri) {
        this.id = id;
        this.date = date;
        this.currencyInteger = currencyInteger;
        this.currencyDecimal = currencyDecimal;
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

    public long getCurrencyDecimal() {
        return currencyDecimal;
    }

    public void setCurrencyDecimal(long currencyDecimal) {
        this.currencyDecimal = currencyDecimal;
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
