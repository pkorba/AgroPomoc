package com.piotrkorba.agropomoc;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    NoteRepository(Application application) {
        AppRoomDatabase db = AppRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNote();
    }

    LiveData<Note> getNote(int id) {
        return mNoteDao.getNote(id);
    }

    LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    LiveData<List<Note>> getNotesBetween(Date startDate, Date endDate) {
        return mNoteDao.getNotesBetween(startDate, endDate);
    }

    public void insert(Note note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    public void update(Note note) {
        new updateAsyncTask(mNoteDao).execute(note);
    }

    public void delete(Note note) {
        new deleteAsyncTask(mNoteDao).execute(note);
    }

    // Async helper classes

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao mAsyncTaskDao;

        updateAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.updateNote(notes[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao mAsyncTaskDao;

        deleteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mAsyncTaskDao.deleteNote(notes[0]);
            return null;
        }
    }
}
