package com.marketo.schemavalidator.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.marketo.schemavalidator.utils.Constants.declaredFieldsMap;

public final class Helper {

    //Retrieves the fields from a model class
    public static Map<String, Map<String, String>> getFields(Class<?> clazz) {
        declaredFieldsMap = new HashMap<>();
        declaredFieldsMap.put(clazz.getSimpleName(), getDeclaredFields(clazz));

        //updated the declaredFieldsMap to match the values coming from swagger (eg:- int -> integer)
        declaredFieldsMap.get(clazz.getSimpleName()).replaceAll((k, v) -> v.equalsIgnoreCase(k) ? "ref" : v);
        declaredFieldsMap.get(clazz.getSimpleName()).replaceAll((k, v) -> v.equals("int") ? "integer" : v);
        declaredFieldsMap.get(clazz.getSimpleName()).replaceAll((k, v) -> v.equalsIgnoreCase("list") ? "array" : v);
        declaredFieldsMap.get(clazz.getSimpleName()).replaceAll((k, v) -> v.equalsIgnoreCase("zoneddatetime") ? "string" : v);
        return declaredFieldsMap;
    }

    private static Map<String, String> getDeclaredFields(Class<?> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, f -> f.getType().getSimpleName().toLowerCase()));
    }

    public static boolean isValid(Map<String, String> first, Map<String, String> second) {
        return first.entrySet().containsAll(second.entrySet());
    }
}
