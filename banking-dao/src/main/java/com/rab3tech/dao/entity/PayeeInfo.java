package com.rab3tech.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="payee_informations_tbl")
public class PayeeInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String payeeAccountNo;
	private String payeeName;
	private String payeeNickName;
	private String customerId;
	private Timestamp doe;
	private Timestamp dom;
	private String remarks;
	private String status;
	private int urn;
	
	public String getCustomerId() {
		return customerId;
	}

	public int getUrn() {
		return urn;
	}

	public void setUrn(int urn) {
		this.urn = urn;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(length=30)
	public String getPayeeAccountNo() {
		return payeeAccountNo;
	}

	public void setPayeeAccountNo(String payeeAccountNo) {
		this.payeeAccountNo = payeeAccountNo;
	}

	@Column(length=100)
	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	@Column(length=100)
	public String getPayeeNickName() {
		return payeeNickName;
	}

	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}
	@CreationTimestamp
	public Timestamp getDoe() {
		return doe;
	}

	public void setDoe(Timestamp doe) {
		this.doe = doe;
	}
	@UpdateTimestamp
	public Timestamp getDom() {
		return dom;
	}

	public void setDom(Timestamp dom) {
		this.dom = dom;
	}

	@Column(length=100)
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(length=100)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "PayeeInfo [id=" + id + ", payeeAccountNo=" + payeeAccountNo + ", payeeName=" + payeeName
				+ ", payeeNickName=" + payeeNickName + ", customerId=" + customerId + ", doe=" + doe + ", dom=" + dom
				+ ", remarks=" + remarks + ", status=" + status + "]";
	}
	
}
