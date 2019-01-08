package de.hsos.bachelorarbeit.nh.endpoint.test.usecase;

import com.google.gson.JsonObject;
import de.hsos.bachelorarbeit.nh.endpoint.test.entities.WatchResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;

import java.io.IOException;

public class Watch {
    String host;
    String port;
    String pathStart;
    String pathStop;
    JsonUtil jsonUtil;

    public Watch(String host, String port, JsonUtil jsonUtil) {
        this.host = host;
        this.port = port;
        String url = "http://" + host + ":" + port;
        pathStart = url + "/watch/start";
        pathStop =  url + "/watch/stop";
        this.jsonUtil = jsonUtil;
    }

    public boolean start() {
        try {
            return jsonUtil.fromJsonURL(pathStart, JsonObject.class).isJsonNull();
        } catch (IOException e) {
            return false;
        }
    }

    public WatchResultGroup stop() {
        try {
            return jsonUtil.fromJsonURL(pathStop, WatchResultGroup.class);
        } catch (IOException e) {
            return null;
        }
    }
}
