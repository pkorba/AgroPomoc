package com.piotrkorba.agropomoc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE = "com.piotrkorba.agropomoc.EXTRA_NOTE";
    public static final String EXTRA_NOTES_LIST = "com.piotrkorba.agropomoc.EXTRA_NOTES_LIST";
    private RecyclerView recyclerView;
    private NoteListAdapter adapter;
    private NoteViewModel mNoteViewModel;
    public static final int NEW_NOTE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerview_notes);
        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        // Add a separator between individual items in RecyclerView
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        mNoteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);

        // Set up observer for all notes.
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.setNotes(notes);
            }
        });
        
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesActivity.this, NewNoteActivity.class);
                startActivityForResult(intent, NEW_NOTE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Note note = (Note) data.getSerializableExtra(NewNoteActivity.EXTRA_NEW_NOTE);
                mNoteViewModel.insert(note);
                Toast.makeText(getApplicationContext(), R.string.note_saved_toast, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.note_not_saved_toast, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == NoteListAdapter.NOTE_DETAIL_REQUEST && resultCode == 11) {
            if (data !=  null) {
                Note updateNote = (Note) data.getSerializableExtra(EXTRA_NOTE);
                if (updateNote != null) {
                    mNoteViewModel.update(updateNote);
                    Toast.makeText(getApplicationContext(), R.string.note_edited_toast, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.note_something_wrong_toast, Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == NoteListAdapter.NOTE_DETAIL_REQUEST && resultCode == 12) {
            if (data !=  null) {
                Note deleteNote = (Note) data.getSerializableExtra(EXTRA_NOTE);
                if (deleteNote != null) {
                    mNoteViewModel.delete(deleteNote);
                    Toast.makeText(getApplicationContext(), R.string.note_deleted_toast, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.note_something_wrong_toast, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_balance:
                Intent intent = new Intent(NotesActivity.this, NoteBalanceActivity.class);
                ArrayList<Note> a = new ArrayList<>(mNoteViewModel.getAllNotes().getValue());
                intent.putExtra(EXTRA_NOTES_LIST, a);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}