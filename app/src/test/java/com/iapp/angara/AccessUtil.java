package com.iapp.angara;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * @author Igor Ivanov
 * @version 1.0
 * Class required to access the
 * private members of the class
 * */
public class AccessUtil {

    /**
     * Method required to access private filed
     * @param object is an instance of the class within which the given field is located
     * @param fieldName is the name of the given field
     * @return link to content field
     * @generic specified what type the field should be
     * */
    public static <T> T getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method required to access to private parameterless constructor
     * @param clazz is type of the object being created
     * @return link to created object
     * @generic safely specifies what type of object being created
     * */
    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true);
            return (T) constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
