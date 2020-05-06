package com.lamin.licenses.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.lamin.licenses.models.Organization;
import com.lamin.licenses.repository.OrganizationRedisRepository;
import com.lamin.licenses.util.UserContext;
import com.lamin.licenses.util.UserContextHolder;

@Component
public class OrganizationRestTemplateClient {

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	OrganizationRedisRepository orgRedisRepo;

	private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);

	private Organization checkRedisCache(String organizationId) {
		
		try {
			return orgRedisRepo.findOrganization(organizationId);
		}
		catch (Exception ex) {
			logger.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}",
					organizationId);
			return null;
		}
	}

	private void cacheOrganizationObject(Organization org) {
		
		try {
			orgRedisRepo.saveOrganization(org);
		} catch (Exception ex) {
			logger.error("Unable to cache organization {} in Redis. Exception {}", org.getId());
		}
	}

	public Organization getOrganization(String organizationId){
		
        logger.debug("In Licensing Service.getOrganization: OrgId : {}, CorrelationId : {} ", organizationId,  UserContextHolder.getContext().getCorrelationId());

        Organization org = checkRedisCache(organizationId);

        if (org!=null){
            logger.debug("Successfully retrieved an organization {} from the redis cache: {}", org, organizationId);
            return org;
        }

        logger.debug("Unable to locate organization from the redis cache: {}.", organizationId);
       
        ResponseEntity<Organization> restExchange = restTemplate.exchange("http://zuul-server/api/org-service/v1/organizations/{organizationId}",
				HttpMethod.GET, null, Organization.class, organizationId); 
        
        org = restExchange.getBody();
        
        logger.debug("Therefore retrieved the organization {} from the organization service: {}", org, organizationId);
        /*Save the record from cache*/
        

        if (org!=null) {
            cacheOrganizationObject(org);
            logger.debug("And caches the retrieved organization {} into Redis caches : {}", organizationId, org);
        }
        return org;
        }
}
	
	
//	@Component
//	public class OrganizationRestTemplateClient {
//
//		private static final Logger logger = LoggerFactory.getLogger(OrganizationRestTemplateClient.class);
//
//		@Autowired
//		RestTemplate restTemplate;
//
//		public Organization getOrganization(String organizationId) {
//
//			logger.info(">>> In Licensing Service.getOrganization: {}. Thread Id: {}",
//					UserContextHolder.getContext().getCorrelationId(), Thread.currentThread().getId());
//			ResponseEntity<Organization> restExchange = restTemplate.exchange(
//					"http://zuul-server/api/org-service/v1/organizations/{organizationId}", HttpMethod.GET, null,
//					Organization.class, organizationId);
//			return restExchange.getBody();
//		}
//
//		public List<Object> getDetectives(String organizationId) {
//
//			ResponseEntity<Object[]> rest = restTemplate.exchange("http://detectives-service/v1/detectives", HttpMethod.GET,
//					null, Object[].class, organizationId);
//
//			return Arrays.asList(rest.getBody());
//		}
//	}