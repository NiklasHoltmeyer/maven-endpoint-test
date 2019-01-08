package de.hsos.bachelorarbeit.nh.endpoint.util.usecases;

import com.google.gson.JsonElement;

import java.io.IOException;

public interface JsonUtil {
    void writeJson(Object object, String path, boolean overwrite) throws IOException;
    void writeJson(Object object, String path) throws IOException;
    <T> T fromJsonFile(String path, Class<T> clazz) throws IOException;
    <T> T fromJsonURL(String url, Class<T> clazz) throws IOException;
    <T> T fromJson(JsonElement jsonElement, Class<T> clazz) throws IOException;
    String toJson(Object o);
}

