package com.iappe.angara.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Access {

    public static  <T> T getField(Object obj, String name) {
        Field mainSoundField;
        try {
            mainSoundField = obj.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        mainSoundField.setAccessible(true);
        try {
            return (T) mainSoundField.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static  <T> T invoke(Object obj, String name, Class[] signature, Object[] options) {
        Method method;
        try {
            method = obj.getClass().getDeclaredMethod(name, signature);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        method.setAccessible(true);
        try {
            return (T) method.invoke(obj, options);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
