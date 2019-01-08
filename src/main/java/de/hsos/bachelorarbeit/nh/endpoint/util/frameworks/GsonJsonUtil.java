package de.hsos.bachelorarbeit.nh.endpoint.util.frameworks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;

import java.io.*;
import java.net.URL;

public class GsonJsonUtil implements JsonUtil {
    @Override
    public void writeJson(Object object, String path) throws IOException{
        writeJson(object,path,true);
    }
    @Override
    public void writeJson(Object object, String path, boolean overWrite) throws IOException{
        try (Writer writer = new FileWriter(path, !overWrite)) {
            Gson gson = this.getGson();
            gson.toJson(object, writer);
        } catch (IOException e) {
            throw e;
        }
    }

    @Override
    public <T> T fromJsonFile(String path, Class<T> clazz) throws IOException{
        JsonReader reader = new JsonReader(new FileReader(path));
        if(reader==null) throw new IOException("Reader-NP");

        Gson gson = this.getGson();
        return new Gson().fromJson(reader, clazz);
    }

    @Override
    public <T> T fromJsonURL(String url, Class<T> clazz) throws IOException{
        InputStreamReader inputStreamReader = this.getInputStream(url);
        Gson gson = this.getGson();
        return gson.fromJson(inputStreamReader, clazz);
    }

    @Override
    public <T> T fromJson(JsonElement jsonElement, Class<T> clazz){
        return this.getGson().fromJson(jsonElement, clazz);
    }

    private Gson getGson(){
        return new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
    }

    private InputStreamReader getInputStream(String url) throws IOException{
        // Quelle: https://stackoverflow.com/a/38546190/5026265
        URL _url = new URL(url);
        InputStreamReader reader = new InputStreamReader(_url.openStream());
        return reader;
    }
}
