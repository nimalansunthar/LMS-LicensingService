package com.lamin.licenses.repository;

import com.lamin.licenses.models.Organization;

public interface OrganizationRedisRepository {

	void saveOrganization(Organization org);

	void updateOrganization(Organization org);

	void deleteOrganization(String organizationId);

	Organization findOrganization(String organizationId);
}