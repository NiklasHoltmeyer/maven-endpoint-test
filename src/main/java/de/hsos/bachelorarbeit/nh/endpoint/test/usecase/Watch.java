package de.hsos.bachelorarbeit.nh.endpoint.test.usecase;

import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.HttpClientHelper;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;

import java.io.IOException;

public class Watch {
    String host;
    String port;
    String pathStart;
    String pathStop;
    JsonUtil jsonUtil;
    HttpClientHelper httpClientHelper;

    public Watch(String host, String port, JsonUtil jsonUtil) {
        this.host = host;
        this.port = port;
        String url = "http://" + host + ":" + port + "/actuator";
        pathStart = url + "/watch/start";
        pathStop =  url + "/watch/stop";
        this.jsonUtil = jsonUtil;
        this.httpClientHelper = new HttpClientHelper(jsonUtil);
    }

    public boolean start() throws IOException{
        return  httpClientHelper.getRequest(pathStart);
        //String s = jsonUtil.fromJsonURL(pathStart, String.class);
        //System.out.println(s);
        //System.exit(0);
        //return jsonUtil.fromJsonURL(pathStart, JsonObject.class) != null;
    }

    public WatchResultGroup stop() throws IOException{
        return jsonUtil.fromJsonURL(pathStop, WatchResultGroup.class);
    }
}
