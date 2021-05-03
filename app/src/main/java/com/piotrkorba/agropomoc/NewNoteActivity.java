package com.piotrkorba.agropomoc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class NewNoteActivity extends AppCompatActivity {

    private ImageButton imageButton;
    private TextView datePicker;
    private Date date;
    private EditText title;
    private EditText currency;
    private EditText content;
    private Button saveButton;
    private SimpleDateFormat fmt;
    private String imageUriStr = "";
    private long currencySign = -1;
    private int id = -1;
    public static final String EXTRA_NEW_NOTE = "com.piotrkorba.agropomoc.NEW_NOTE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        imageButton = findViewById(R.id.noteImageButton);
        datePicker = findViewById(R.id.noteDatePicker);
        title = findViewById(R.id.noteTitleEditText);
        currency = findViewById(R.id.noteCurrencyEditText);
        content = findViewById(R.id.noteContentEditText);
        saveButton = findViewById(R.id.noteSaveButton);
        date = Calendar.getInstance().getTime();
        fmt = new SimpleDateFormat("dd.MM.yyyy");
        datePicker.setText(fmt.format(date));
        // Set input filter to allow only valid currency than can be held in two long values
        InputFilter[] ifCurrency = {new InputFilterCurrency(18, 2)};
        currency.setFilters(ifCurrency);

        Intent intent = getIntent();
        // Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey(NotesActivity.EXTRA_NOTE)) {
                Note newNote = (Note) intent.getSerializableExtra(NotesActivity.EXTRA_NOTE);
                id = newNote.getId();
                title.setText(newNote.getTitle());
                content.setText(newNote.getContent());
                datePicker.setText(fmt.format(newNote.getDate()));
                date = newNote.getDate();
                long currencyInteger = newNote.getCurrencyInteger();
                long currencyDecimal = newNote.getCurrencyDecimal();
                currency.setText(String.valueOf(Math.abs(currencyInteger)) + "." + String.valueOf(Math.abs(currencyDecimal)));
                if (currencyInteger >= 0 || currencyDecimal >= 0) {
                    RadioButton radioButton = findViewById(R.id.notesRadioButtonIncome);
                    currencySign = 1;
                    radioButton.toggle();
                }
                if (!newNote.getImageUri().isEmpty()) {
                    imageButton.setVisibility(View.VISIBLE);
                    imageButton.setImageURI(Uri.parse(newNote.getImageUri()));
                }
            }
        }
    }

    public void saveNote(View view) {
        String titleStr = title.getText().toString();
        String currencyStr = currency.getText().toString();
        String contentStr = content.getText().toString();

        Intent replyIntent = new Intent();
        if (titleStr.isEmpty() && currencyStr.isEmpty() && contentStr.isEmpty()) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            long currencyInteger = 0;
            long currencyDecimal = 0;
            if (!currencyStr.isEmpty()) {
                String[] currencyStrArr = currencyStr.split("[.]", 0);
                currencyInteger = Long.parseLong(currencyStrArr[0]) * currencySign;
                if (currencyStrArr.length > 1) {
                    currencyDecimal = Long.parseLong(currencyStrArr[1]) * currencySign;
                }
            }
            Note note;
            if (id == -1) {
                note = new Note(date, currencyInteger, currencyDecimal, titleStr, contentStr, imageUriStr);
            } else {
                note = new Note(id, date, currencyInteger, currencyDecimal, titleStr, contentStr, imageUriStr);
            }
            replyIntent.putExtra(EXTRA_NEW_NOTE, note);
            setResult(RESULT_OK, replyIntent);
        }
        finish();
    }

    public void processDatePickerResult(int year, int month, int day) {
        String dayStr = Integer.toString(day);
        String monthStr = Integer.toString(month + 1);
        String yearStr = Integer.toString(year);
        String dateStr = (dayStr + "." + monthStr + "." + yearStr);
        datePicker.setText(dateStr);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        date = new Date(cal.getTimeInMillis());
        datePicker.setText(fmt.format(date));
    }

    public void showDatePicker(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(),"datePicker");
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // Check which radio button was clicked.
        switch (view.getId()) {
            case R.id.notesRadioButtonExpense:
                if (checked)
                    currencySign = -1;
                break;
            case R.id.notesRadioButtonIncome:
                if (checked)
                    currencySign = 1;
                break;
            default:
                // Do nothing.
                break;
        }
    }

    public void chooseImage(View view) {
        Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (data != null) {
                try {
                    final Uri imageUri = data.getData();
                    getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    imageUriStr = imageUri.toString();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageButton.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(NewNoteActivity.this, "Coś poszło nie tak", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(NewNoteActivity.this, "Nie wybrałeś obrazu", Toast.LENGTH_LONG).show();
            }
        }
    }
}