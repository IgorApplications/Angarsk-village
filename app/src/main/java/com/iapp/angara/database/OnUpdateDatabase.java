package com.iapp.angara.database;

import java.util.List;

public interface OnUpdateDatabase<T extends Element> {

    void onUpdate(List<T> elements);
}
