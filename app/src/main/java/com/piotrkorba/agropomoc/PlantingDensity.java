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
    Calculator.Unit mUnit = Calculator.Unit.plantpHa;
    String[] unitArray;

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

        // Create the spinner.
        Spinner spinner = findViewById(R.id.calc4result_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create an ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.plantDensity, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
        unitArray = this.getResources().getStringArray(R.array.plantDensity);
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
        if (spinnerLabel.equals(unitArray[0])) {
            mUnit = Calculator.Unit.plantpHa;
        } else if (spinnerLabel.equals(unitArray[1])) {
            mUnit = Calculator.Unit.plantpm2;
        }
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
                result.setText(df.format(Calculator.areaDensityConverter(resultDb, mUnit)));
            } catch (NumberFormatException e) {
                // Fail silently
            }
        } else {
            result.setText("–");
        }
    }
}