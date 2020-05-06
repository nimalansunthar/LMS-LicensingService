package com.lamin.licenses.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import org.springframework.cloud.client.discovery.DiscoveryClient;

@Service
public class DiscoveryService {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    public List getEurekaServices(){

       List<String> services = new ArrayList<String>();

        discoveryClient.getServices().forEach(serviceName -> {

            discoveryClient.getInstances(serviceName).forEach(instance->{

                services.add( String.format("%s:%s",serviceName,instance.getUri()));

            });

        });
        return services;
    }
}