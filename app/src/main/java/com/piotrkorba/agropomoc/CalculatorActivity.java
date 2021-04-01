package com.piotrkorba.agropomoc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
    }

    public void calcItemClick(View view) {
        TextView textView = (TextView) view;
        int id = textView.getId();
        Intent intent;
        /* TODO: Create and launch calculator target acitivities
        switch (id) {
            case R.id.calculator_item1:
                intent = new Intent(this, SeedRateActivity.class);
                startActivity(intent);
                break;
            case R.id.calculator_item2:
                intent = new Intent(this, PotentialYieldCereal.class);
                startActivity(intent);
                break;
            case R.id.calculator_item3:
                intent = new Intent(this, PotentialYieldLegumes.class);
                startActivity(intent);
                break;
            case R.id.calculator_item4:
                intent = new Intent(this, PlantingDensity.class);
                startActivity(intent);
                break;
            case R.id.calculator_item5:
                intent = new Intent(this, DryingLosses.class);
                startActivity(intent);
                break;
            default:
                // Do nothing
        }
        */
    }
}