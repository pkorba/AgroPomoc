package com.piotrkorba.agropomoc;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterMinMax implements InputFilter {
    private int mMin, mMax;

    public InputFilterMinMax(int min, int max) {
        mMin = min;
        mMax = max;
    }

    public InputFilterMinMax(String min, String max) {
        mMin = Integer.parseInt(min);
        mMax = Integer.parseInt(max);
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            double input = Double.parseDouble(dest.toString() + source.toString());
            if (isInRange(mMin, mMax, input))
                return null;
        } catch (NumberFormatException e) {
            // Do nothing
        }
        return "";
    }

    private boolean isInRange(double min, double max, double value) {
        if (min <= max && value >= min && value <= max)
            return true;
        return false;
    }
}
