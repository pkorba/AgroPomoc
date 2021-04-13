package com.piotrkorba.agropomoc;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for a single note.
 */
@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Update
    void updateNote(int id);

    @Query("DELETE FROM notes_table WHERE id = :id")
    void deleteNote(int id);

    @Query("SELECT id, date, currencyInteger, currenctDecimal, title, content, imageUri FROM notes_table WHERE id = :id")
    LiveData<Note> getNote(int id);

    @Query("SELECT id, date, currencyInteger, currenctDecimal, title, content, imageUri FROM notes_table")
    LiveData<List<Note>> getAllNote();

    @Query("SELECT id, date, currencyInteger, currenctDecimal, title, content, imageUri FROM notes_table WHERE date BETWEEN :startDate AND :endDate")
    LiveData<Note> getNotesBetween(Date startDate, Date endDate);
}