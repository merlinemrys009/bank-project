package com.rab3tech.payeeInfo.service.impl;

import java.util.List;

import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;

public interface PayeeInfoService {

	String savePayeeInfo(PayeeInfoVO payeeInfoVO);

	List<PayeeInfoVO> findAllData(String customerId);

	PayeeInfoVO findPayeeInfoById(int payeeInfoId);

	void editPayee(PayeeInfoVO payeeInfoVO) throws Exception;

	void deletePayee(int payeeInfoId) throws Exception;

	List<PayeeInfoVO> findData(String customerId);

	String transferOfFunds(TransactionVO transactionVO);

	List<TransactionVO> viewStatement(String customerId);



}
