package com.iapp.angara.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.iapp.angara.database.Element;


public class ElementAdapter<T extends Element> extends ArrayAdapter<T> {

    private final int resId;
    private final OnChangeElement<T> onChange;

    public ElementAdapter(Context context, int resId, OnChangeElement<T> onChange) {
        super(context, resId);
        this.resId = resId;
        this.onChange = onChange;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(resId, parent, false);
        }

        T current = getItem(position);
        onChange.onChange(listItemView, current);

        return listItemView;
    }
}
