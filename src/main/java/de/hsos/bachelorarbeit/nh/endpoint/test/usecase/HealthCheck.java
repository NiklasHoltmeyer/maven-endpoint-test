package de.hsos.bachelorarbeit.nh.endpoint.test.usecase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HealthCheck {

    /**
     * Timeout in MS
     */
    int timeOut = 1000;
    int maxIterations = 300;

    String host;
    int port=80;
    String path="/";

    public HealthCheck(String host, int port, String path, int timeOut, int maxIterations) {
        this.timeOut = timeOut;
        this.maxIterations=maxIterations;
        this.host=host;
        this.port=port;
        this.path=path;
    }

    public HealthCheck(String host, int port, String path){
        this.host=host;
        this.port=port;
        this.path=path;
    }

    public HealthCheck(String host, int port){
        this.host=host;
        this.port=port;
    }

    public HealthCheck(String host){
        this.host=host;
    }

    public boolean isAccessable(){
        return isAccessable(path);
    }

    public boolean isAccessable(String relative){
        String url = host + ":" + port + relative;
        if(!url.startsWith("http")) url = "http://"+url;
        int start = maxIterations;
        while(maxIterations-- >= 0){
            if(_isAccessable(url, timeOut)){
                return true;
            }
        }
        return false;
    }

    private boolean _isAccessable(String url, int timeout) {
        //Quelle https://stackoverflow.com/a/13778801/5026265
        url = url.replaceFirst("https", "http"); // Otherwise an exception may
        // be thrown on invalid SSL
        // certificates.

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url)
                    .openConnection();
            connection.setConnectTimeout(timeout);
            connection.setReadTimeout(timeout);
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return false;
            }
        } catch (IOException exception) {
            return false;
        }
        return true;
    }
}
