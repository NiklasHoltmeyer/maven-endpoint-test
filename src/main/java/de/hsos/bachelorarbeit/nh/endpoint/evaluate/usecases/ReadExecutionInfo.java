package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ReadExecutionInfo {
    private static final String ENDPOINTREPORTRELATIVEPATH = "acturator-executeinfo.json";
    protected static String ENDPOINTREPORTABSOLOUTEPATH;
    private JsonUtil jsonUtil;
    public ReadExecutionInfo(String reportPath, JsonUtil jsonUtil) throws FileNotFoundException {
        ENDPOINTREPORTABSOLOUTEPATH = Paths.get(reportPath, "executioninfo",ENDPOINTREPORTRELATIVEPATH).toAbsolutePath().toString();
        if(!new File(ENDPOINTREPORTABSOLOUTEPATH).exists()) throw new FileNotFoundException("Report-Missing: " + ENDPOINTREPORTABSOLOUTEPATH);
        this.jsonUtil = jsonUtil;
    }

    public List<EndpointGroupInfo> getEndPointGroupInfo() throws IOException {
        JsonArray jr = jsonUtil.fromJsonFile(ENDPOINTREPORTABSOLOUTEPATH, JsonArray.class);
        List<EndpointGroupInfo> endpointGroupInfos = new ArrayList<>();
        for (int i = 0; i < jr.size(); ++i) {
            JsonElement jE = jr.get(i);
            EndpointGroupInfo endpointGroupInfo = jsonUtil.fromJson(jE, EndpointGroupInfo.class);
            endpointGroupInfos.add(endpointGroupInfo);
        }
        return endpointGroupInfos;
    }
}

