package com.rab3tech.vo;

import java.util.Date;

public class AddressVO {
	
	private int id;
	private String loginId;
	private String address1;
	private String address2;
	private String city;
    private String state;
    private String zipcode;
    private String firstName;
    private String lastName;
    private Date DoE;
    private Date DoM;
    
    public int getId() {
    	return id;
    }
    public void setId(int id) {
    	this.id = id;
	}
    
    public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getAddress1() {
    	return address1;
	}
    public void setAddress1(String address1) {
    	this.address1 = address1;
	}
    public String getAddress2() {
		return address2;
    }
    public void setAddress2(String address2) {
		this.address2 = address2;
    }
    public String getCity() {
    	return city;
	}
    public void setCity(String city) {
    	this.city = city;
	}
    public String getState() {
    	return state;
	}
    public void setState(String state) {
    	this.state = state;
	}
    public String getZipcode() {
    	return zipcode;
	}
    public void setZipcode(String zipcode) {
    	this.zipcode = zipcode;
	}
    public String getFirstName() {
    	return firstName;
	}
    public void setFirstName(String firstName) {
	
    	this.firstName = firstName;
	}
    
    public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Date getDoE() {
    	return DoE;
	}
    public void setDoE(Date doE) {
    	DoE = doE;
	}
	public Date getDoM() {
		return DoM;
	}
	public void setDoM(Date doM) {
		DoM = doM;
	}
	@Override
	public String toString() {
		return "AddressVO [id=" + id + ", loginId=" + loginId + ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", DoE=" + DoE + ", DoM=" + DoM + "]";
	}
	
}
