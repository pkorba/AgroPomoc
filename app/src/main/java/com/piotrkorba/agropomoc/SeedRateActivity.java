package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
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

public class SeedRateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText tgw, population, seedEmergency, fieldEmergency;
    TextView result;
    DecimalFormat df;
    Calculator.Unit mUnit = Calculator.Unit.kgpHa;
    String[] unitArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_rate);

        result = findViewById(R.id.calc1result);
        result.setText("–");
        tgw = findViewById(R.id.calc1tgw);
        population = findViewById(R.id.calc1population);
        seedEmergency = findViewById(R.id.calc1seed_emergency);
        fieldEmergency = findViewById(R.id.calc1field_emergency);

        // Add listeners to EditText fields
        tgw.addTextChangedListener(textWatcher);
        population.addTextChangedListener(textWatcher);
        seedEmergency.addTextChangedListener(textWatcher);
        fieldEmergency.addTextChangedListener(textWatcher);

        // Set input filter on percentage input fields (max 100%)
        InputFilter ifMinMax[] = {new InputFilterMinMax(1, 100)};
        seedEmergency.setFilters(ifMinMax);
        fieldEmergency.setFilters(ifMinMax);

        // Set up result formatter and rounding mode
        df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setRoundingMode(RoundingMode.DOWN);

        // Create the spinner.
        Spinner spinner = findViewById(R.id.calc1result_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }

        // Create an ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.areaDensity, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }
        unitArray = this.getResources().getStringArray(R.array.areaDensity);
    }

    TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String tgwStr = tgw.getText().toString();
            String populationStr = population.getText().toString();
            String seedEmergencyStr = seedEmergency.getText().toString();
            String fieldEmergencyStr = fieldEmergency.getText().toString();
            if (!tgwStr.isEmpty() && !populationStr.isEmpty() && !seedEmergencyStr.isEmpty() && !fieldEmergencyStr.isEmpty()) {
                try {
                    double tgwDb = Double.parseDouble(tgwStr);
                    double populationDb = Double.parseDouble(populationStr);
                    double seedEmergencyDb = Double.parseDouble(seedEmergencyStr);
                    double fieldEmergencyDb = Double.parseDouble(fieldEmergencyStr);
                    double resultDb = Calculator.seedRate(populationDb, tgwDb, seedEmergencyDb, fieldEmergencyDb);
                    result.setText(df.format(Calculator.areaDensityConverter(resultDb, mUnit)));
                } catch (NumberFormatException e) {
                    // Fail silently
                }
            } else {
                result.setText("–");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String spinnerLabel = parent.getItemAtPosition(position).toString();
        if (spinnerLabel.equals(unitArray[0])) {
            mUnit = Calculator.Unit.kgpHa;
        } else if (spinnerLabel.equals(unitArray[1])) {
            mUnit = Calculator.Unit.kgpa;
        } else if (spinnerLabel.equals(unitArray[2])) {
            mUnit = Calculator.Unit.tpHa;
        } else if (spinnerLabel.equals(unitArray[3])) {
            mUnit = Calculator.Unit.tpa;
        }
        String tgwStr = tgw.getText().toString();
        String populationStr = population.getText().toString();
        String seedEmergencyStr = seedEmergency.getText().toString();
        String fieldEmergencyStr = fieldEmergency.getText().toString();
        if (!tgwStr.isEmpty() && !populationStr.isEmpty() && !seedEmergencyStr.isEmpty() && !fieldEmergencyStr.isEmpty()) {
            try {
                double tgwDb = Double.parseDouble(tgwStr);
                double populationDb = Double.parseDouble(populationStr);
                double seedEmergencyDb = Double.parseDouble(seedEmergencyStr);
                double fieldEmergencyDb = Double.parseDouble(fieldEmergencyStr);
                double resultDb = Calculator.seedRate(populationDb, tgwDb, seedEmergencyDb, fieldEmergencyDb);
                result.setText(df.format(Calculator.areaDensityConverter(resultDb, mUnit)));
            } catch (NumberFormatException e) {
                // Fail silently
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }
}