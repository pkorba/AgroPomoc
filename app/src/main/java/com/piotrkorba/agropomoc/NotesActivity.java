package com.piotrkorba.agropomoc;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class NotesActivity extends AppCompatActivity {
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

        // TODO: open add/edit existing/new note activity
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
                String title = data.getStringExtra(NewNoteActivity.EXTRA_TITLE);
                String content = data.getStringExtra(NewNoteActivity.EXTRA_CONTENT);
                Date date = (Date) data.getSerializableExtra(NewNoteActivity.EXTRA_DATE);
                long currencyInteger = data.getLongExtra(NewNoteActivity.EXTRA_CURRENCY_INTEGER, 0);
                long currencyDecimal = data.getLongExtra(NewNoteActivity.EXTRA_CURRENCY_DECIMAL, 0);
                Note note = new Note(date, currencyInteger, currencyDecimal, title, content, "");
                mNoteViewModel.insert(note);
            }
        } else {
            Toast.makeText(getApplicationContext(), "Nie zapisano notatki", Toast.LENGTH_LONG).show();
        }
    }
}