package com.piotrkorba.agropomoc;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Date;
import java.util.List;

/**
 * Data Access Object (DAO) for a single note.
 */
@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);

    @Update
    void updateNote(Note note);

    @Delete
    void deleteNote(Note note);

    @Query("SELECT id, date, currencyInteger, currencyDecimal, title, content, imageUri FROM notes_table WHERE id = :id")
    LiveData<Note> getNote(int id);

    @Query("SELECT id, date, currencyInteger, currencyDecimal, title, content, imageUri FROM notes_table ORDER BY date DESC")
    LiveData<List<Note>> getAllNote();

    @Query("SELECT id, date, currencyInteger, currencyDecimal, title, content, imageUri FROM notes_table WHERE date BETWEEN :startDate AND :endDate")
    LiveData<List<Note>> getNotesBetween(Date startDate, Date endDate);
}
