package com.lamin.licenses.models;

import java.io.Serializable;

import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;

public class Organization extends JdkSerializationRedisSerializer implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1902621170368241854L;
	String id;
	String name;
	String contactName;
	String contactEmail;
	String contactPhone;	

	public Organization() {		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public String getContactPhone() {
		return contactPhone;
	}

	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}
}
