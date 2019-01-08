package de.hsos.bachelorarbeit.nh.endpoint.util.usecases;

import java.io.IOException;

public interface JsonUtil {
    void writeJson(Object object, String path, boolean overwrite) throws IOException;
    void writeJson(Object object, String path) throws IOException;
    <T> T readJsonFromFile(String path, Class<T> clazz) throws IOException;
    <T> T getJson(String url, Class<T> clazz) throws IOException;
}
