package com.iapp.angara.util;

@FunctionalInterface
public interface Filter<E> {

    boolean test(E e);
}
