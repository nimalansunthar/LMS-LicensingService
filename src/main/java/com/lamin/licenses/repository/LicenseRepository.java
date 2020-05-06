package com.lamin.licenses.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lamin.licenses.models.License;

@Repository
public interface LicenseRepository extends JpaRepository<License, String> {

	public List<License> findByOrganizationId(String organizationId);

	public License findByOrganizationIdAndLicenseId(String organizationId, String licenseId);

}
