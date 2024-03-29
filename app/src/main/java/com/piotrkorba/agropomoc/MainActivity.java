package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Force populate database
        AppRoomDatabase.getDatabase(getApplication());
    }

    public void onClickSorButton(View view) {
        Intent intent = new Intent(this, SorActivity.class);
        startActivity(intent);
    }

    public void onClickCalculatorButton(View view) {
        Intent intent = new Intent(this, CalculatorActivity.class);
        startActivity(intent);
    }

    public void onCLickNotesButton(View view) {
        Intent intent = new Intent(this, NotesActivity.class);
        startActivity(intent);
    }
}