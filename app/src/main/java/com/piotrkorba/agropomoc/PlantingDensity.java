package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class PlantingDensity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText rowDistance, plantDistance;
    TextView result;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planting_density);
        result = findViewById(R.id.calc4result);
        result.setText("–");
        plantDistance = findViewById(R.id.calc4plant_distance);
        rowDistance = findViewById(R.id.calc4row_distance);

        // Add listeners to EditText fields
        plantDistance.addTextChangedListener(textWatcher);
        rowDistance.addTextChangedListener(textWatcher);
        // Set up result formatter and rounding mode
        df = new DecimalFormat("#", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setRoundingMode(RoundingMode.DOWN);
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            calculationPerform();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerLabel = parent.getItemAtPosition(position).toString();
        calculationPerform();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public void calculationPerform() {
        String rowDistanceStr = rowDistance.getText().toString();
        String plantDistanceStr = plantDistance.getText().toString();
        if (!rowDistanceStr.isEmpty() && !plantDistanceStr.isEmpty()) {
            try {
                double rowDistanceDb = Double.parseDouble(rowDistanceStr);
                double plantDistanceDb = Double.parseDouble(plantDistanceStr);
                double resultDb = Calculator.panting(rowDistanceDb, plantDistanceDb);
                result.setText(df.format(resultDb));
            } catch (NumberFormatException e) {
                // Fail silently
            }
        } else {
            result.setText("–");
        }
    }
}