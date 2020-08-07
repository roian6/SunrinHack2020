package com.david0926.sunrinhack2020.util;

import android.view.View;

import androidx.databinding.BindingConversion;

public class BindingOptions {

    @BindingConversion
    public static int convertBooleanToVisibility(boolean visible) {
        return visible ? View.VISIBLE : View.GONE;
    }

}
