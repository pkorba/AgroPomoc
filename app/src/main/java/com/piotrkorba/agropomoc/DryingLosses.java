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

public class DryingLosses extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText initialWeight, initialMoisture, finalMoisture;
    TextView result, weightLoss;
    DecimalFormat df;
    Calculator.Unit resultUnit = Calculator.Unit.KILOGRAM;
    Calculator.Unit weightLossUnit = Calculator.Unit.KILOGRAM;
    Calculator.Unit initialWeightUnit = Calculator.Unit.KILOGRAM;
    String[] unitArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drying_losses);

        result = findViewById(R.id.calc5result);
        result.setText("–");
        weightLoss = findViewById(R.id.calc5weight_loss);
        weightLoss.setText("–");
        initialWeight = findViewById(R.id.calc5initial_weight);
        initialMoisture = findViewById(R.id.calc5initial_moisture);
        finalMoisture = findViewById(R.id.calc5final_moisture);

        // Add listeners to EditText fields
        initialWeight.addTextChangedListener(textWatcher);
        initialMoisture.addTextChangedListener(textWatcher);
        finalMoisture.addTextChangedListener(textWatcher);

        // Set input filter on percentage input fields (max 99%)
        InputFilter ifMinMax[] = {new InputFilterMinMax(0, 99)};
        initialMoisture.setFilters(ifMinMax);
        finalMoisture.setFilters(ifMinMax);

        // Set up result formatter and rounding mode
        df = new DecimalFormat("#.##", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        df.setRoundingMode(RoundingMode.DOWN);

        // Create the spinners.
        Spinner spinnerResult = findViewById(R.id.calc5result_spinner);
        Spinner spinnerWeightLoss = findViewById(R.id.calc5weight_loss_spinner);
        Spinner spinnerInitialWeight = findViewById(R.id.calc5weight_spinner);
        if (spinnerResult != null) {
            spinnerResult.setOnItemSelectedListener(this);
        }
        if (spinnerWeightLoss != null) {
            spinnerWeightLoss.setOnItemSelectedListener(this);
        }
        if (spinnerInitialWeight != null) {
            spinnerInitialWeight.setOnItemSelectedListener(this);
        }

        // Create an ArrayAdapter using the string array and default spinner layout.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.weightUnits, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinners.
        if (spinnerResult != null) {
            spinnerResult.setAdapter(adapter);
        }
        if (spinnerWeightLoss != null) {
            spinnerWeightLoss.setAdapter(adapter);
        }
        if (spinnerInitialWeight != null) {
            spinnerInitialWeight.setAdapter(adapter);
        }
        unitArray = this.getResources().getStringArray(R.array.weightUnits);
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
        switch (parent.getId()) {
            case R.id.calc5result_spinner:
                if (spinnerLabel.equals(unitArray[0])) {
                    resultUnit = Calculator.Unit.KILOGRAM;
                } else if (spinnerLabel.equals(unitArray[1])) {
                    resultUnit = Calculator.Unit.TONNE;
                }
                break;
            case R.id.calc5weight_loss_spinner:
                if (spinnerLabel.equals(unitArray[0])) {
                    weightLossUnit = Calculator.Unit.KILOGRAM;
                } else if (spinnerLabel.equals(unitArray[1])) {
                    weightLossUnit = Calculator.Unit.TONNE;
                }
                break;
            case R.id.calc5weight_spinner:
                if (spinnerLabel.equals(unitArray[0])) {
                    initialWeightUnit = Calculator.Unit.KILOGRAM;
                } else if (spinnerLabel.equals(unitArray[1])) {
                    initialWeightUnit = Calculator.Unit.TONNE;
                }
                break;
            default:
                // Do nothing
        }
        calculationPerform();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing
    }

    public void calculationPerform() {
        String initialWeightStr = initialWeight.getText().toString();
        String initialMoistureStr = initialMoisture.getText().toString();
        String finalMoistureStr = finalMoisture.getText().toString();
        if (!initialWeightStr.isEmpty() && !initialMoistureStr.isEmpty() && !finalMoistureStr.isEmpty()) {
            try {
                double initialWeightDb = Double.parseDouble(initialWeightStr);
                double initialMoistureDb = Double.parseDouble(initialMoistureStr);
                double finalMoistureDb = Double.parseDouble(finalMoistureStr);
                if (initialWeightUnit == Calculator.Unit.TONNE) {
                    initialWeightDb *= 1000;
                }
                double resultDb = Calculator.weightLossOnDrying(initialWeightDb, initialMoistureDb, finalMoistureDb);
                double weightLossDb = initialWeightDb - resultDb;
                result.setText(df.format(Calculator.weightConverter(resultDb, resultUnit)));
                weightLoss.setText(df.format(Calculator.weightConverter(weightLossDb, weightLossUnit)));
            } catch (NumberFormatException e) {
                // Fail silently
            }
        } else {
            result.setText("–");
            weightLoss.setText("–");
        }
    }
}