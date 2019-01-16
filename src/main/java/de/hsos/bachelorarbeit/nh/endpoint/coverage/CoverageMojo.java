package de.hsos.bachelorarbeit.nh.endpoint.coverage;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.entities.Request;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.frameworks.jmeter.JMeter;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.frameworks.spring.SpringUTIL;
import de.hsos.bachelorarbeit.nh.endpoint.coverage.usecases.generateCoverage.GenerateCoverage;
import de.hsos.bachelorarbeit.nh.endpoint.util.ClassLoaderLoader;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage.CoverageResult;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.coverage.CoverageResultAggregated;
import de.hsos.bachelorarbeit.nh.endpoint.util.frameworks.GsonJsonUtil;
import de.hsos.bachelorarbeit.nh.endpoint.util.usecases.JsonUtil;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Mojo(name = "coverage", defaultPhase = LifecyclePhase.TEST, threadSafe = true)
public class CoverageMojo extends AbstractMojo {
    @Parameter(required = true, readonly =  true)
    private String destination;

    @Parameter(required = true, readonly =  true)
    private List<String> basePackages;

    @Parameter(defaultValue = "${project}")
    private MavenProject project;

    @Parameter(defaultValue = "0", required = true)
    private int minimumCoverage;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {       
        String baseDir = project.getBasedir().getAbsolutePath();
        String jmxAbsolutePath = (destination.startsWith(baseDir)) ? Paths.get(destination).toString() :  Paths.get(baseDir, destination).toString(); // (jmx=absolute)? jmx : absolutePath(jmx)
        if(!jmxAbsolutePath.endsWith(".jmx")) jmxAbsolutePath = Paths.get(jmxAbsolutePath, "all-endpoints.jmx").toAbsolutePath().toString();

        Thread.currentThread().setContextClassLoader(new ClassLoaderLoader().getClassLoader(project));

        JsonUtil jsonUtil = new GsonJsonUtil();

        try {
            runEndpointCoverage(baseDir, jmxAbsolutePath, jsonUtil);
        } catch (Exception e) {
            throw new MojoFailureException(e.toString());
        }
    }

    private void runEndpointCoverage(String baseDir, String jmxAbsolutePath, JsonUtil jsonUtil) throws Exception{
        HashMap<Request, Integer> requestIntegerHashMap;
        List<Endpoint> endpoints = new SpringUTIL().getEndpoints(this.basePackages);

        try {
            requestIntegerHashMap = new JMeter(jmxAbsolutePath).getAllRequests();
        } catch (IOException e) {
            String error = new StringBuilder()
                    .append("JMeter::getAllRequests - JMX Missing?")
                    .append(System.getProperty("line.separator"))
                    .append("IOException: ")
                    .append(System.getProperty("line.separator"))
                    .append(System.getProperty("line.separator"))
                    .append(e.toString())
                    .toString();
            throw new IOException(error);
        }

        GenerateCoverage generateCoverage = new GenerateCoverage(getLog(), endpoints, requestIntegerHashMap);
        CoverageResult coverageResult = generateCoverage.loadResult();
        int threshHold = minimumCoverage < 1? minimumCoverage*100 : minimumCoverage; //range 0 - 100
        boolean result = coverageResult.buildSuccessful(threshHold);
        if(result){
            getLog().info("***********************************");
            getLog().info("***** J-Meter Test GenerateCoverage! ******");
            getLog().info("***********************************");
            getLog().info(coverageResult.toString());
            getLog().info("minimumCoverage: " + threshHold);
            getLog().info("Actual: " + coverageResult.getCoverage());
            getLog().info("***********************************");
            getLog().info("**** J-Meter GenerateCoverage-Success! ****");
            getLog().info("***********************************");
        }else{
            getLog().error("***********************************");
            getLog().error("***** J-Meter Test GenerateCoverage! ******");
            getLog().error("***********************************");
            getLog().error("***********************************");
            getLog().error("basePackages:    " + basePackages);
            getLog().error("jmxAbsolutePath: " + jmxAbsolutePath);
            getLog().error("***********************************");
            getLog().error(coverageResult.toString());
            getLog().error("minimumCoverage: " + threshHold);
            getLog().error("Actual: " + coverageResult.getCoverage());
            getLog().error("***********************************");
            getLog().error("**** J-Meter GenerateCoverage-Failed! *****");
            getLog().error("***********************************");
        }

        if(!result){
            throw new Exception("Target GenerateCoverage: " + threshHold + "%, Actual: " + coverageResult.getCoverage());
        }else{
            String baseP =Paths.get(destination, "/reports/coverage/").toAbsolutePath().toString();
            new File(baseP).mkdirs();
            String resultPath = Paths.get(baseP, "endpointcoverage.json").toAbsolutePath().toString();
            String aggregatedResultPath = Paths.get(baseP, "endpointcoverage-aggregated.json").toAbsolutePath().toString();

            CoverageResultAggregated coverageResultAggregated = new CoverageResultAggregated(coverageResult);

            jsonUtil.writeJson(coverageResult, resultPath, true);
            jsonUtil.writeJson(coverageResultAggregated, aggregatedResultPath, true);
        }
    }



}
