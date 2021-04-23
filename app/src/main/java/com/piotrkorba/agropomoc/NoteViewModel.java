package com.piotrkorba.agropomoc;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;
    private LiveData<List<Note>> mAllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    LiveData<Note> getNote(int id) {
        return mRepository.getNote(id);
    }

    LiveData<List<Note>> getNotesBetween(Date startDate, Date endDate) {
        return mRepository.getNotesBetween(startDate, endDate);
    }

    public void insert(Note note) {
        mRepository.insert(note);
    }

    public void update(Note note) {
        mRepository.update(note);
    }

    public void delete(Note note) {
        mRepository.delete(note);
    }
}
