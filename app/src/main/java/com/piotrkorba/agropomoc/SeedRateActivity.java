package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class SeedRateActivity extends AppCompatActivity {

    EditText tgw, population, seedEmergency, fieldEmergency;
    TextView result;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seed_rate);
        result = findViewById(R.id.calc1result);
        tgw = findViewById(R.id.calc1tgw);
        population = findViewById(R.id.calc1population);
        seedEmergency = findViewById(R.id.calc1seed_emergency);
        fieldEmergency = findViewById(R.id.calc1field_emergency);
        tgw.addTextChangedListener(textWatcher);
        population.addTextChangedListener(textWatcher);
        seedEmergency.addTextChangedListener(textWatcher);
        fieldEmergency.addTextChangedListener(textWatcher);
        InputFilter ifMinMax[] = {new InputFilterMinMax(1, 100)};
        seedEmergency.setFilters(ifMinMax);
        fieldEmergency.setFilters(ifMinMax);
        df = new DecimalFormat("#.####", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setRoundingMode(RoundingMode.DOWN);
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
                    result.setText(df.format(resultDb));
                } catch (NumberFormatException e) {
                    // Fail silently
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    //TODO: Create inputfilter for percentage fields
}