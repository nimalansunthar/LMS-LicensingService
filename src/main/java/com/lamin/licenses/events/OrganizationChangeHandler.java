package com.lamin.licenses.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

import com.lamin.licenses.repository.OrganizationRedisRepository;

@EnableBinding(CustomChannels.class)
public class OrganizationChangeHandler {

	private static final Logger logger = LoggerFactory.getLogger(OrganizationChangeHandler.class);
	
	@Autowired
	private OrganizationRedisRepository organizationRedisRepository;	

	@StreamListener("inboundOrgChanges")
    public void updateRedisCache(OrganizationChangeModel orgChange) {

        logger.debug("Received a message of type " + orgChange.getType());

        switch(orgChange.getAction()){

            case "GET":
                logger.debug("Received a GET event from the organization service for organization id {}", orgChange.getOrganizationId());
                break;
                
            case "SAVE":
            	logger.debug("Received a SAVE event from the organization service for organization id {}", orgChange.getOrganizationId());
                break;

            case "UPDATE":
                logger.debug("Received a UPDATE event from the organization service for organization id {}", orgChange.getOrganizationId());
                organizationRedisRepository.deleteOrganization(orgChange.getOrganizationId());
                logger.debug("Successfully deleted the organization with id :{} in Redis cache!", orgChange.getOrganizationId());
                break;

            case "DELETE":
                logger.debug("Received a DELETE event from the organization service for organization id {}", orgChange.getOrganizationId());
                organizationRedisRepository.deleteOrganization(orgChange.getOrganizationId());
                logger.debug("Successfully deleted the organization with id :{} in Redis cache!", orgChange.getOrganizationId());
                break;

            default:
                logger.error("Received an UNKNOWN event from the organization service of type {}", orgChange.getType());
                break;
            }
	}
}