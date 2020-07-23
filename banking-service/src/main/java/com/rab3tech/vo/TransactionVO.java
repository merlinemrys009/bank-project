package com.rab3tech.vo;

import java.util.Date;

public class TransactionVO {
	
	private float amount;
	private String description;
	private String debitAccountNumber;
	private int payeeId;
	private String customerId;
	private Date transactionDate;
	private String transactionType;
	private PayeeInfoVO pInfoVO;
	

	public Date getTransactionDate() {
		return transactionDate;
	}


	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}


	public String getTransactionType() {
		return transactionType;
	}


	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}


	public PayeeInfoVO getpInfoVO() {
		return pInfoVO;
	}


	public void setpInfoVO(PayeeInfoVO pInfoVO) {
		this.pInfoVO = pInfoVO;
	}


	public float getAmount() {
		return amount;
	}


	public void setAmount(float amount) {
		this.amount = amount;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getPayeeId() {
		return payeeId;
	}


	public void setPayeeId(int payeeId) {
		this.payeeId = payeeId;
	}


	public String getCustomerId() {
		return customerId;
	}


	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}


	public String getDebitAccountNumber() {
		return debitAccountNumber;
	}


	public void setDebitAccountNumber(String debitAccountNumber) {
		this.debitAccountNumber = debitAccountNumber;
	}


	@Override
	public String toString() {
		return "TransactionVO [amount=" + amount + ", description=" + description + ", debitAccountNumber="
				+ debitAccountNumber + ", payeeId=" + payeeId + ", customerId=" + customerId + ", transactionDate="
				+ transactionDate + ", transactionType=" + transactionType + ", pInfoVO=" + pInfoVO + "]";
	}


}
