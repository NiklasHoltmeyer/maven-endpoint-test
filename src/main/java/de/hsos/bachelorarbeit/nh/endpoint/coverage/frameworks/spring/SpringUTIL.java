package de.hsos.bachelorarbeit.nh.endpoint.coverage.frameworks.spring;

import de.hsos.bachelorarbeit.nh.endpoint.coverage.usecases.generateCoverage.EndPointUTIL;
import de.hsos.bachelorarbeit.nh.endpoint.util.entities.Endpoint;
import org.reflections.Reflections;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SpringUTIL extends EndPointUTIL {
    public SpringUTIL(){}
    public List<Endpoint> getEndpoints(String basePackage){
        return this.getRESTController(basePackage)
                .stream()
                .flatMap(this::getEndpoints)
                .collect(Collectors.toList());
    }


    private Set<Class<?>> getRESTController(String basePackage){
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(RequestMapping.class);
    }

    private Stream<Endpoint> getEndpoints(Class<?> clazz){
        List<Endpoint> endpoints = new ArrayList<>();

        RequestMapping[] requestMappings = clazz.getAnnotationsByType(RequestMapping.class);
        String baseURL = requestMappings[0].value().length > 0 ? requestMappings[0].value()[0] : "";
        for(Method m : clazz.getDeclaredMethods()){
            RequestMapping requestMapping = m.getAnnotation(RequestMapping.class);
            if(requestMapping!=null){
                String[] paths = requestMapping.value();
                RequestMethod[] methods = requestMapping.method();

                for(int i = 0; i < paths.length; ++i){
                    endpoints.add(new Endpoint(baseURL + paths[i], methods[i].name()));
                }
            }

        }

        return  endpoints.stream();
    }
}
