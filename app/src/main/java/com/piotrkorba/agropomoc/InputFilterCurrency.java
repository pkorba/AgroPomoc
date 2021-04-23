package com.piotrkorba.agropomoc;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputFilterCurrency implements InputFilter {
    Pattern mPattern;
    private int mMaxIntPlaces, mMaxDecPlaces;

    public InputFilterCurrency(int maxIntPlaces, int maxDecPlaces) {
        mMaxIntPlaces = maxIntPlaces;
        mMaxDecPlaces = maxDecPlaces;
        mPattern = Pattern.compile("^[0-9]{1," + mMaxIntPlaces + "}.?(([0-9]{1," + mMaxDecPlaces + "})?)$");
    }

    public InputFilterCurrency(String maxIntPlaces, String maxDecPlaces) {
        mMaxIntPlaces = Integer.parseInt(maxIntPlaces);
        mMaxDecPlaces = Integer.parseInt(maxDecPlaces);
        mPattern = Pattern.compile("^[0-9]{1," + mMaxIntPlaces + "}.?(([0-9]{1," + mMaxDecPlaces + "})?)$");
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        Matcher matcher = mPattern.matcher(dest.toString() + source.toString());
        if (matcher.matches()) {
            return null;
        }
        return "";
    }
}