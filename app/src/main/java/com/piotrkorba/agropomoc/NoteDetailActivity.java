package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;

public class NoteDetailActivity extends AppCompatActivity {
    private ImageButton imageButton;
    private TextView date;
    private TextView title;
    private TextView content;
    private TextView currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);
        imageButton = findViewById(R.id.noteDetailImageButton);
        date = findViewById(R.id.noteDetailDateTextView);
        title = findViewById(R.id.noteDetailTitleTextView);
        content = findViewById(R.id.noteDetailContentTextView);
        currency = findViewById(R.id.noteDetailCurrencyTextView);
        Intent intent = getIntent();
        Note note = (Note) intent.getSerializableExtra(NotesActivity.EXTRA_NOTE);
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
}