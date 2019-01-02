package de.hsos.bachelorarbeit.nh.endpoint.evaluate.frameworks.gson;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.*;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases.ReadExecutionInfo;
import org.apache.maven.plugin.logging.Log;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadEndPointGroupInfos implements ReadExecutionInfo {
    String jsonPath;
    Log log;

    public ReadEndPointGroupInfos(Log log, String reportPath) {
        this.jsonPath = Paths.get(reportPath, "acturator.json").toString();
        this.log = log;
    }

    @Override
    public List<EndpointGroupInfo> getEndPointGroupInfo() {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(jsonPath));
        } catch (FileNotFoundException e) {}

        if(reader==null) return new ArrayList<>();

        Gson gson = new Gson();
        EndpointGroupInfo[] egis = new Gson().fromJson(reader, EndpointGroupInfo[].class);

        return Arrays.asList(egis);
    }
}
