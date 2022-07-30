package com.iapp.angara.ui;

import android.view.View;

import com.iapp.angara.database.Element;

public interface OnChangeElement<T extends Element> {

    void onChange(View view, T t);
}
