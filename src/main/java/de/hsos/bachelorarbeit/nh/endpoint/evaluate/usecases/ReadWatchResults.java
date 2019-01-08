package de.hsos.bachelorarbeit.nh.endpoint.evaluate.usecases;

import de.hsos.bachelorarbeit.nh.endpoint.test.entities.WatchResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Unit;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.WatchResultGroup;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadWatchResults {
    JsonUtil jsonUtil;
    Path ssvPath;

    public ReadWatchResults(JsonUtil jsonUtil, String reportPath) throws FileNotFoundException {
        this.jsonUtil = jsonUtil;
        ssvPath = Paths.get(reportPath, "watchTests", "watch.ssv");
        init();
    }
    private void init() throws FileNotFoundException{
        File f = new File(ssvPath.toAbsolutePath().toString());
        if(!f.exists()) throw new FileNotFoundException("Missing-Report-SSV:" + ssvPath);
    }

    public List<WatchResultGroup> getWatchResultGroup() throws IOException{
        try (Stream<String> stream = Files.lines(ssvPath).skip(1)) {
            return  stream.map(this::stringToWatchResult)
                    .collect(Collectors.toList());
        }
    }

    private WatchResultGroup stringToWatchResult(String line) {
        //path method cpuAVG cpuAVGUnit memAVG memAVG-Unit"
        WatchResultGroup wR = null;
        if(line!=null&&!line.isEmpty()){
            String[] cols = line.split(" ");
            if(cols.length>5){
                String path         = cols[0];
                String method       = cols[1];
                String cpuAVG       = cols[2];
                String cpuAVGUnit   = cols[3];
                String memAVG       = cols[4];
                String memAVGUnit   = cols[5];

                WatchResult<Double> cpu = new WatchResult<>();
                WatchResult<Double> mem = new WatchResult<>();
                cpu.setAverage(new Unit<Double>(Double.valueOf(cpuAVG), cpuAVGUnit));
                mem.setAverage(new Unit<Double>(Double.valueOf(memAVG), memAVGUnit));
                wR = new WatchResultGroup(cpu, mem, path, method);
            }else{
                System.out.println("1) " + line);
            }
        }else{
            System.out.println("2) " + line);
        }
        return wR;
    }


}
