package com.example.samto.architectureexample;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

// This class retrieves and makes data available to the view model. Cool.

public class NoteRepository {
    private NoteDao noteDao; // Need this to access the data
    private LiveData<List<Note>> allNotes;

    // Constructor to get everything set up and ready
    public NoteRepository (Application application) {
        NoteDatabase db = NoteDatabase.getInstance(application);
        noteDao = db.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    // Now our API

    public void insert(Note note) {
       new InsertNoteAsyncTask(noteDao).execute(note);
    }


    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);

    }

    public void deleteAllNotes() {
        new DeleteAllNotesAsyncTask(noteDao).execute();

    }

    public void update(Note note) {
        new UpdateAsyncTask(noteDao).execute(note);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;

    }

    // Need to set up Asynctasks for these. Ooooh boy

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        //We need to use a constructor to pass the noteDao

        public InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }

    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        //We need to use a constructor to pass the noteDao

        public DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        //We need to use a constructor to pass the noteDao

        public DeleteAllNotesAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteAllNotes();
            return null;
        }
    }
    private static class UpdateAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        //We need to use a constructor to pass the noteDao

        public UpdateAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
}
