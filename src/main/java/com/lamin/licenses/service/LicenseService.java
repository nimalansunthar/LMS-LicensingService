package com.lamin.licenses.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lamin.licenses.clients.OrganizationRestTemplateClient;
import com.lamin.licenses.models.License;
import com.lamin.licenses.models.Organization;
import com.lamin.licenses.repository.LicenseRepository;
import com.lamin.licenses.util.UserContextHolder;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

@Service
public class LicenseService {

	private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);
	
    @Autowired
    private LicenseRepository licenseRepository;

//    @Autowired
//    ServiceConfig config;  

    @Autowired
    OrganizationRestTemplateClient organizationRestClient;
    
    @HystrixCommand(fallbackMethod = "buildFallbackLicense", commandProperties = {@HystrixProperty( name="execution.isolation.thread.timeoutInMilliseconds", value="9000")})
    public License getLicense(String organizationId,String licenseId) throws InterruptedException {

    	//Thread.sleep(11000);
    	logger.debug("LicenseService.getLicenseByLicenseId  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);        
        
        Organization org = organizationRestClient.getOrganization(organizationId);

        return license
                .withOrganizationName( org.getName())
                .withContactName( org.getContactName())
                .withContactEmail( org.getContactEmail() )
                .withContactPhone( org.getContactPhone() );
               // .withComment(config.getExampleProperty());
    }
    
    private License buildFallbackLicense(String organizationId, String licenseId){   
		
		License license = new License().withId("0000000-00-00000").withOrganizationId( organizationId )
				.withProductName("Sorry no licensing information currently available");
		return license; 
	}

    @HystrixCommand(fallbackMethod = "buildFallbackLicenseList", commandProperties = {@HystrixProperty( name="execution.isolation.thread.timeoutInMilliseconds", value="9000")})
    public List<License> getLicensesByOrg(String organizationId) throws InterruptedException{
    	
    	logger.debug("LicenseService.getLicensesByOrg  Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        return licenseRepository.findByOrganizationId(organizationId );
    }
    
    private List<License> buildFallbackLicenseList(String organizationId){   

		List<License> fallbackList = new ArrayList<>();  
		License license = new License().withId("0000000-00-00000").withOrganizationId( organizationId )
				.withProductName("Sorry no licensing information currently available");

			fallbackList.add(license);  return fallbackList; 
	}

    public void saveLicense(License license){

        license.withId( UUID.randomUUID().toString());
        licenseRepository.save(license);
    }

    public void updateLicense(License license){
    	
      licenseRepository.save(license);
    }

    public void deleteLicense(License license){

        licenseRepository.deleteById(license.getLicenseId());
    }
}