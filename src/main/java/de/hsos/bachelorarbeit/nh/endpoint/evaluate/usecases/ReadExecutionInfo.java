package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.evaluate.entities.ExecutionInfo.EndpointGroupInfo;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReadExecutionInfo {
    private static final String ENDPOINTREPORTRELATIVEPATH = "acturator-executeinfo.json";
    protected static String ENDPOINTREPORTABSOLOUTEPATH;
    private JsonUtil jsonUtil;
    public ReadExecutionInfo(String reportPath, JsonUtil jsonUtil) throws FileNotFoundException {
        ENDPOINTREPORTABSOLOUTEPATH = Paths.get(reportPath, ENDPOINTREPORTRELATIVEPATH).toAbsolutePath().toString();
        if(!new File(ENDPOINTREPORTABSOLOUTEPATH).exists()) throw new FileNotFoundException("Report-Missing: " + ENDPOINTREPORTABSOLOUTEPATH);
        this.jsonUtil = jsonUtil;
    }

    public List<EndpointGroupInfo> getEndPointGroupInfo() {
        /*JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(ENDPOINTREPORTABSOLOUTEPATH));
        } catch (FileNotFoundException e) {}

        if(reader==null) return new ArrayList<>();

        Gson gson = new Gson();
        EndpointGroupInfo[] egis = new Gson().fromJson(reader, EndpointGroupInfo[].class);
*/
        EndpointGroupInfo[] egis = new EndpointGroupInfo[0];
        try {
            egis = this.jsonUtil.readJsonFromFile(ENDPOINTREPORTABSOLOUTEPATH, EndpointGroupInfo[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return Arrays.asList(egis);
    }
}
