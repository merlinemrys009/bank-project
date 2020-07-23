package com.rab3tech.vo;

import java.util.Date;

import org.yaml.snakeyaml.Yaml;


public class CustomerAccountInfoVO {
	private long id;
	private LoginVO customerId;
	private String accountNumber;
	private String currency;
	private String branch;
	private float tavBalance;
	private float avBalance;
	private Date StatusAsOf;
	private AccountTypeVO accountType;
	private AccountStatusVO accountStatus;
	private String accountType1;

	public AccountStatusVO getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(AccountStatusVO accountStatus) {
		this.accountStatus = accountStatus;
	}

	public String getAccountType1() {
		return accountType1;
	}

	public void setAccountType1(String accountType1) {
		this.accountType1 = accountType1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LoginVO getCustomerId() {
		return customerId;
	}

	public void setCustomerId(LoginVO customerId) {
		this.customerId = customerId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public float getTavBalance() {
		return tavBalance;
	}

	public void setTavBalance(float tavBalance) {
		this.tavBalance = tavBalance;
	}

	public float getAvBalance() {
		return avBalance;
	}

	public void setAvBalance(float avBalance) {
		this.avBalance = avBalance;
	}

	public Date getStatusAsOf() {
		return StatusAsOf;
	}

	public void setStatusAsOf(Date statusAsOf) {
		StatusAsOf = statusAsOf;
	}

	public AccountTypeVO getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountTypeVO accountType) {
		this.accountType = accountType;
	}

	@Override
	public String toString() {
		return "CustomerAccountInfoVO [id=" + id + ", customerId=" + customerId + ", accountNumber=" + accountNumber
				+ ", currency=" + currency + ", branch=" + branch + ", tavBalance=" + tavBalance + ", avBalance="
				+ avBalance + ", StatusAsOf=" + StatusAsOf + ", accountType=" + accountType + ", accountStatus="
				+ accountStatus + ", accountType1=" + accountType1 + "]";
	}
	

}
