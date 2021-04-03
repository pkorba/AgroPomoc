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

public class PotentialYieldCereal extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText tgw, population, grainAmount;
    TextView result;
    DecimalFormat df;
    Calculator.Unit mUnit = Calculator.Unit.kgpHa;
    String[] unitArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_potential_yield_cereal);

        result = findViewById(R.id.calc2result);
        result.setText("–");
        tgw = findViewById(R.id.calc2tgw);
        population = findViewById(R.id.calc2population);
        grainAmount = findViewById(R.id.calc2grain_amount);

        // Add listeners to EditText fields
        tgw.addTextChangedListener(textWatcher);
        population.addTextChangedListener(textWatcher);
        grainAmount.addTextChangedListener(textWatcher);

        // Set up result formatter and rounding mode
        df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setRoundingMode(RoundingMode.DOWN);

        // Create the spinner.
        Spinner spinner = findViewById(R.id.calc2result_spinner);
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
            mUnit = Calculator.Unit.kgpHa;
        } else if (spinnerLabel.equals(unitArray[1])) {
            mUnit = Calculator.Unit.kgpa;
        } else if (spinnerLabel.equals(unitArray[2])) {
            mUnit = Calculator.Unit.tpHa;
        } else if (spinnerLabel.equals(unitArray[3])) {
            mUnit = Calculator.Unit.tpa;
        }
        calculationPerform();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public void calculationPerform() {
        String tgwStr = tgw.getText().toString();
        String populationStr = population.getText().toString();
        String grainAmountStr = grainAmount.getText().toString();
        if (!tgwStr.isEmpty() && !populationStr.isEmpty() && !grainAmountStr.isEmpty()) {
            try {
                double tgwDb = Double.parseDouble(tgwStr);
                double populationDb = Double.parseDouble(populationStr);
                double grainAmountDb = Double.parseDouble(grainAmountStr);
                double resultDb = Calculator.potentialYieldCereal(grainAmountDb, tgwDb, populationDb);
                result.setText(df.format(Calculator.areaDensityConverter(resultDb, mUnit)));
            } catch (NumberFormatException e) {
                // Fail silently
            }
        } else {
            result.setText("–");
        }
    }
}