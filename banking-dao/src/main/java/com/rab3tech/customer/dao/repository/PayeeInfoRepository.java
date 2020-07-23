package com.rab3tech.customer.dao.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.PayeeInfo;

public interface PayeeInfoRepository extends JpaRepository<PayeeInfo, String> {

	Optional<PayeeInfo> findByPayeeNickName(String payeeNickName);
	Optional<PayeeInfo> findByPayeeAccountNo(String payeeAccountNo);

	List<PayeeInfo> findByCustomerId(String customerId);
	void deleteById(int payeeInfoId);
	Optional<PayeeInfo> findById(int payeeInfoId);
//	Optional<PayeeInfo> findById(String customerId);
	List<PayeeInfo> findUserByCustomerId(String customerId);

}
