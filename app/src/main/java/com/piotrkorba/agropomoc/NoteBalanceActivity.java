package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class NoteBalanceActivity extends AppCompatActivity {
    private ArrayList<Note> notesList;
    private TextView datePicker1;
    private TextView datePicker2;
    private TextView incomeTextView;
    private TextView expenseTextView;
    private TextView balanceTextView;
    private Button button;
    Date date1, date2;
    SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_balance);
        Intent intent = getIntent();
        notesList = (ArrayList<Note>) intent.getSerializableExtra(NotesActivity.EXTRA_NOTES_LIST);
        datePicker1 = findViewById(R.id.noteBalanceDate1TextView);
        datePicker2 = findViewById(R.id.noteBalanceDate2TextView);
        incomeTextView = findViewById(R.id.noteBalanceIncomeTextView);
        incomeTextView.setText("—");
        expenseTextView = findViewById(R.id.noteBalanceExpenseTextView);
        expenseTextView.setText("—");
        balanceTextView = findViewById(R.id.noteBalanceBalanceTextView);
        balanceTextView.setText("—");
        button = findViewById(R.id.noteBalanceButton);
        date1 = new Date(0);
        date2 = Calendar.getInstance().getTime();
        datePicker1.setText(fmt.format(date1));
        datePicker2.setText(fmt.format(date2));
    }

    public void showDatePicker(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (view.getId() == R.id.noteBalanceDate1TextView) {
            DatePickerDialog firstD = new DatePickerDialog(NoteBalanceActivity.this, new innerFirstDate(), year, month, day);
            firstD.show();
        } else if (view.getId() == R.id.noteBalanceDate2TextView) {
            DatePickerDialog secondD = new DatePickerDialog(NoteBalanceActivity.this, new innerSecondDate(), year, month, day);
            secondD.show();
        }
    }

    public void calculateBalance(View view) {
        long incomeInteger, incomeDecimal, expenseInteger, expenseDecimal, balanceInteger, balanceDecimal, currencyInteger, currencyDecimal;
        incomeInteger = incomeDecimal = expenseInteger = expenseDecimal = balanceInteger = balanceDecimal = 0;
        for (Note n: notesList) {
            if (n.getDate().after(date1) && n.getDate().before(date2)) {
                currencyInteger = n.getCurrencyInteger();
                currencyDecimal = n.getCurrencyDecimal();
                if (currencyInteger < 0 || currencyDecimal < 0) {
                    expenseInteger += currencyInteger;
                    expenseDecimal += currencyDecimal;
                } else {
                    incomeInteger += currencyInteger;
                    incomeDecimal += currencyDecimal;
                }
            }
        }

        expenseInteger += expenseDecimal / 100;
        expenseDecimal %= 100;

        incomeInteger += incomeDecimal / 100;
        incomeDecimal %= 100;

        balanceInteger = incomeInteger + expenseInteger;
        balanceDecimal = incomeDecimal + expenseDecimal;

        balanceInteger += balanceDecimal / 100;
        balanceDecimal %= 100;

        String incomeStr = String.format("%d.%2d", incomeInteger, Math.abs(incomeDecimal)).replace(" ", "0");
        incomeStr = incomeStr + " zł";
        incomeTextView.setText(incomeStr);
        String expenseStr = String.format("%d.%2d", Math.abs(expenseInteger), Math.abs(expenseDecimal)).replace(" ", "0");
        expenseStr = expenseStr + " zł";
        expenseTextView.setText(expenseStr);
        String balanceStr = String.format("%d.%2d", balanceInteger, Math.abs(balanceDecimal)).replace(" ", "0");
        balanceStr = balanceStr + " zł";
        balanceTextView.setText(balanceStr);
    }

    class innerFirstDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //Doing thing with first Date pick Dialog
            String dayStr = Integer.toString(dayOfMonth);
            String monthStr = Integer.toString(monthOfYear + 1);
            String yearStr = Integer.toString(year);
            String dateStr = (dayStr + "." + monthStr + "." + yearStr);
            datePicker1.setText(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date1 = new Date(cal.getTimeInMillis());
            datePicker1.setText(fmt.format(date1));
        }

    }

    class innerSecondDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            //Doing thing with second Date Picker Dialog
            String dayStr = Integer.toString(dayOfMonth);
            String monthStr = Integer.toString(monthOfYear + 1);
            String yearStr = Integer.toString(year);
            String dateStr = (dayStr + "." + monthStr + "." + yearStr);
            datePicker2.setText(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date2 = new Date(cal.getTimeInMillis());
            datePicker2.setText(fmt.format(date2));
        }
    }
}