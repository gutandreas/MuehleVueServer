package edu.andreasgut.MuehleWebSpringVue.Tools;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class JSONTools {

    private static final Gson gson = new Gson();

    public static String getJsonStringOfObject(Object object){
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {

        try {
            return gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException("JSON konnte nicht in Java-Objekt des entsprechenden Typs umgewandelt werden", e);
        }
    }
}
