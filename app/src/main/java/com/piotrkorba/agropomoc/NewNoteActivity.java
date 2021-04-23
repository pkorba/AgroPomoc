package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

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
    private long currencySign = -1;
    public static final String EXTRA_TITLE = "com.piotrkorba.agropomoc.TITLE";
    public static final String EXTRA_CONTENT = "com.piotrkorba.agropomoc.CONTENT";
    public static final String EXTRA_DATE = "com.piotrkorba.agropomoc.DATE";
    public static final String EXTRA_CURRENCY_INTEGER = "com.piotrkorba.agropomoc.CURRENCY_INTEGER";
    public static final String EXTRA_CURRENCY_DECIMAL = "com.piotrkorba.agropomoc.CURRENCY_DECIMAL";

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
    }

    public void saveNote(View view) {
        String titleStr = title.getText().toString();
        String currencyStr = currency.getText().toString();
        String contentStr = content.getText().toString();

        Intent replyIntent = new Intent();
        if (titleStr.isEmpty() && currencyStr.isEmpty() && contentStr.isEmpty()) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            String[] currencyStrArr = currencyStr.split("[.]", 0);
            long currencyInteger = Long.parseLong(currencyStrArr[0]) * currencySign;
            long currencyDecimal = 0;
            if (currencyStrArr.length > 1) {
                currencyDecimal = Long.parseLong(currencyStrArr[1]) * currencySign;
            }
            replyIntent.putExtra(EXTRA_TITLE, titleStr);
            replyIntent.putExtra(EXTRA_DATE, date);
            replyIntent.putExtra(EXTRA_CONTENT, contentStr);
            replyIntent.putExtra(EXTRA_CURRENCY_INTEGER, currencyInteger);
            replyIntent.putExtra(EXTRA_CURRENCY_DECIMAL, currencyDecimal);
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
}