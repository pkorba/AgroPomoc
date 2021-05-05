package com.piotrkorba.agropomoc;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class NoteDetailActivity extends AppCompatActivity {
    public static final int EDIT_NOTE_ACTIVITY_REQUEST_CODE = 11;
    private ImageButton imageButton;
    private TextView date;
    private TextView title;
    private TextView content;
    private TextView currency;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.noteDetailToolbar);
        myToolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(myToolbar);

        imageButton = findViewById(R.id.noteDetailImageButton);
        date = findViewById(R.id.noteDetailDateTextView);
        title = findViewById(R.id.noteDetailTitleTextView);
        content = findViewById(R.id.noteDetailContentTextView);
        currency = findViewById(R.id.noteDetailCurrencyTextView);
        Intent intent = getIntent();
        note = (Note) intent.getSerializableExtra(NotesActivity.EXTRA_NOTE);
        if (!note.getImageUri().isEmpty()) {
            imageButton.setVisibility(View.VISIBLE);
            imageButton.setImageURI(Uri.parse(note.getImageUri()));
        }

        SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
        date.setText(fmt.format(note.getDate()));
        title.setText(note.getTitle());
        content.setText(note.getContent());
        long currencyInteger = note.getCurrencyInteger();
        long currencyDecimal = note.getCurrencyDecimal();
        currency.setText(String.valueOf(currencyInteger) + "." + String.valueOf(Math.abs(currencyDecimal)) + " zł");

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imageUri = note.getImageUri();
                if (!imageUri.isEmpty()) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse(imageUri), "image/*");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.note_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_edit:
                Intent intent = new Intent(NoteDetailActivity.this, NewNoteActivity.class);
                intent.putExtra(NotesActivity.EXTRA_NOTE, note);
                startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE);
                return true;
            case R.id.note_delete:
                new AlertDialog.Builder(this)
                    .setTitle(R.string.note_detail_dialog_title)
                    .setMessage(R.string.note_detail_dialog_message)
                    .setPositiveButton(R.string.note_detail_dialog_positive_button, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            if (note != null) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(NotesActivity.EXTRA_NOTE, note);
                                setResult(12, resultIntent);
                                finish();
                            }
                        }
                    })

                    // A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(R.string.note_detail_dialog_negative_button, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Note editNote = (Note) data.getSerializableExtra(NewNoteActivity.EXTRA_NEW_NOTE);
                Intent resultIntent = new Intent();
                resultIntent.putExtra(NotesActivity.EXTRA_NOTE, editNote);
                setResult(11, resultIntent);
                finish();
            }
        }
    }
}