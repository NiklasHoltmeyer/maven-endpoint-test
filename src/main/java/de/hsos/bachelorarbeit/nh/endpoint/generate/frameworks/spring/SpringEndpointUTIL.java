package de.hsos.bachelorarbeit.nh.endpoint.generate.frameworks.spring;


import de.hsos.bachelorarbeit.nh.endpoint.generate.entities.RESTEndpoint;
import de.hsos.bachelorarbeit.nh.endpoint.generate.entities.Request;
import de.hsos.bachelorarbeit.nh.endpoint.generate.usecases.getRequests.EndpointUTIL;
import de.hsos.bachelorarbeit.nh.jmeter.annotation.EndpointTest;
import de.hsos.bachelorarbeit.nh.jmeter.annotation.Request.Parameter;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class SpringEndpointUTIL extends EndpointUTIL {
    protected Set<Class<?>> getRESTController(String basePackage){
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(RequestMapping.class);
    }
    protected Stream<RESTEndpoint> getEndpoints(Class<?> clazz) {
        List<RESTEndpoint> endpoints = new ArrayList<>();

        RequestMapping[] requestMappings = clazz.getAnnotationsByType(RequestMapping.class);
        String baseURL = requestMappings[0].value().length > 0 ? requestMappings[0].value()[0] : "";

        for (Method m : clazz.getDeclaredMethods()) {
            EndpointTest endpointTest = m.getAnnotation(EndpointTest.class);
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            if (endpointTest != null && requestMapping != null) {
                String[] paths = requestMapping.value();
                RequestMethod[] methods = requestMapping.method();
                for (int i = 0; i < paths.length; ++i) {
                    endpoints.add(new RESTEndpoint(baseURL + paths[i], methods[i].name(), endpointTest));
                }
            }
        }
        return  endpoints.stream();
    }
    private List<String> splitPath(String path){
        List<String> result = new ArrayList<>();
        String[] paths = path.split("/");
        for(String s : paths){
            if(!s.isEmpty()){
                result.add(s);
            }
        }
        return result;
    }
    private String joinPath(List<String> splittedPath, Parameter[] parameters){
        StringBuilder stringBuilder = new StringBuilder();
        splittedPath.forEach(x->{
            stringBuilder.append("/");
            if(x.startsWith("{")){
                String key = this.removeFirstAndLastChar(x);
                String value = this.getVariable(key, parameters);
                stringBuilder.append(value);
            }else{
                stringBuilder.append(x);
            }
        });
        return stringBuilder.append("/").toString();
    }
    private String removeFirstAndLastChar(String s){
        return s.substring(1, s.length()-1);
    }
    private String getVariable(String variableName, Parameter[] parameters){
        for(Parameter parameter : parameters){
            if(parameter.key().equals(variableName)){
                return parameter.value();
            }
        }
        throw new NullPointerException(variableName + ", not found!");
    }

    protected Request parseRequest(RESTEndpoint restEndpoint){
        List<String> splitedPath = this.splitPath(restEndpoint.getPath());
        String joinPath = this.joinPath(splitedPath, restEndpoint.getEndpointTest().request().parameters());
        return new Request(joinPath, restEndpoint);
    }

}
