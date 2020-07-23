package com.rab3tech.dao.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="customer_address")
public class Address {
	private int id;
	private Login loginId;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;
	private String firstName;
	private String lastName;
	private Date DoE;
	private Date DoM;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "loginId", nullable = false)
	public Login getLoginId() {
		return loginId;
	}
	public void setLoginId(Login loginId) {
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
		return "Address [id=" + id + ", loginId=" + loginId + ", address1=" + address1 + ", address2=" + address2
				+ ", city=" + city + ", state=" + state + ", zipcode=" + zipcode + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", DoE=" + DoE + ", DoM=" + DoM + "]";
	}
	
}
