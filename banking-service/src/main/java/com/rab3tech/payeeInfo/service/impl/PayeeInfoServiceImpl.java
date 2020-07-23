package com.rab3tech.payeeInfo.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.AccountRepository;
import com.rab3tech.customer.dao.repository.AddressRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.customer.dao.repository.PayeeInfoRepository;
import com.rab3tech.customer.dao.repository.TransactionRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.PayeeInfo;
import com.rab3tech.dao.entity.Transaction;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.TransactionVO;

@Service
@Transactional
public class PayeeInfoServiceImpl implements PayeeInfoService {

	@Autowired
	PayeeInfoService payeeInfoService;

	@Autowired
	PayeeInfoRepository payeeInfoRepository;

	@Autowired
	AccountRepository accountRepository;

	@Autowired
	AccountService accountService;
	
	@Autowired
	TransactionRepository transactionReository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	AddressRepository addressRepository;

	@Override
	public String savePayeeInfo(PayeeInfoVO payeeInfoVO) {
		String message = null;
		Optional<CustomerAccountInfoVO> cAccount = accountService.findByUser(payeeInfoVO.getCustomerId());//ya account server link ha  
		if (cAccount.isPresent()) {
//			CustomerAccountInfoVO accountInfoVO = cAccount.get();
//			if (accountInfoVO.getAccountStatus().getCode().equalsIgnoreCase("AS04")) {//AC001 nhi ya AS04 and above are active account na
//			message = "You do not have a valid Account.";
//			return message;
//		}
		Optional<PayeeInfo> optional1 = payeeInfoRepository.findByPayeeNickName(payeeInfoVO.getPayeeNickName());
		if (optional1.isPresent()) {
			message = "Beneficiary with same Nick Name already exists.";
			return message;
		}
		Optional<CustomerAccountInfo> optional = accountRepository.findByAccountNumber(payeeInfoVO.getPayeeAccountNo());
		if (optional.isPresent()) {
			message = "Payee Account Number is valid";

		} else {
			message = "Payee Account Number is Invalid.";
		}
		CustomerAccountInfo accountInfo = optional.get();
		if (!"AC001".equalsIgnoreCase(accountInfo.getAccountType().getCode())) {
			message = "Payee Account is not Active.";
			return message;
		}
		if (accountInfo.getCustomerId().getLoginid().equalsIgnoreCase(payeeInfoVO.getCustomerId())) {
			message = "You can not Add your own account as Beneficiary.";
			return message;
		}
		
		Optional<PayeeInfo> acnt = payeeInfoRepository.findByPayeeAccountNo(payeeInfoVO.getPayeeAccountNo());
		if (acnt.isPresent()) {
			message = "Beneficary with same Account already exists.";
			return message;
		}

		PayeeInfo payeeInfo = new PayeeInfo();
		BeanUtils.copyProperties(payeeInfoVO, payeeInfo);
		payeeInfo.setDoe(new Timestamp(new Date().getTime()));
		payeeInfo.setDom(new Timestamp(new Date().getTime()));
		payeeInfo.setStatus("AS04");
		payeeInfo.setRemarks(payeeInfoVO.getRemarks());
		payeeInfoRepository.save(payeeInfo);
		message = "Your Payee has been successfully Added";
		

	}
		return message;
}

	@Override
	public List<PayeeInfoVO> findAllData(String customerId) {
		List<PayeeInfo>payeeInfo=payeeInfoRepository.findByCustomerId(customerId);
		List<PayeeInfoVO>payeeInfoVOs= new ArrayList<PayeeInfoVO>();
		for(PayeeInfo payeeInfo2:payeeInfo) {
			PayeeInfoVO payeeInfoVO=new PayeeInfoVO();
			BeanUtils.copyProperties(payeeInfo2, payeeInfoVO);
			payeeInfoVOs.add(payeeInfoVO);
		}
		return payeeInfoVOs;
	}

	@Override
	public PayeeInfoVO findPayeeInfoById(int payeeInfoId) {
		PayeeInfoVO payeeInfoVO=new PayeeInfoVO();
		PayeeInfo info=payeeInfoRepository.findById(payeeInfoId).get();
		BeanUtils.copyProperties(info, payeeInfoVO);
		return payeeInfoVO;
	}

	

	@Override
	public void editPayee(PayeeInfoVO payeeInfoVO) throws Exception {
		PayeeInfo payeeInfo=payeeInfoRepository.findById(payeeInfoVO.getId()).get();
		BeanUtils.copyProperties(payeeInfoVO, payeeInfo, Utils.ignoreNullData(payeeInfoVO));
		payeeInfoRepository.save(payeeInfo);
		
	}
	
	@Override
	public void deletePayee(int payeeInfoId) throws Exception {
		payeeInfoRepository.deleteById(payeeInfoId);
		
	}
	
	//Fund Transfer
	@Override
	public List<PayeeInfoVO> findData(String customerId) {
		List<PayeeInfo> payeeInfos=payeeInfoRepository.findUserByCustomerId(customerId);
		List<PayeeInfoVO> vos=new ArrayList<>();
		for(PayeeInfo vInfo: payeeInfos) {
			PayeeInfoVO payeeInfoVO=new PayeeInfoVO();
			BeanUtils.copyProperties(vInfo, payeeInfoVO);
			vos.add(payeeInfoVO);
		}
		return vos;
	}
	//Transaction 
	@Override
	public String transferOfFunds(TransactionVO transactionVO) {
		
		String msg= null;
		Optional<Customer> customer=customerRepository.findByEmail(transactionVO.getCustomerId());
		Optional<CustomerAccountInfo>acnt=accountRepository.findByCustomerId(customer.get().getLogin());
		CustomerAccountInfo debitor= new CustomerAccountInfo();
		if (acnt.isPresent()) {
			debitor = acnt.get();
			transactionVO.setDebitAccountNumber(debitor.getAccountNumber());
//			if (!"AS04".equalsIgnoreCase(debitor.getAccountStatus().getCode())) {
//				msg = "You don't have Active Account.";
//				return msg;
//			}
			if (!"AC001".equalsIgnoreCase(debitor.getAccountType().getCode())) {
				msg="You don't have saving Account";
				return msg;
			}
			
			
			if (debitor.getAvBalance()<transactionVO.getAmount()) {
				msg="Amount can not Exceed of Current Amount $" +debitor.getAvBalance();
				return msg;
				
			}
				debitor.setAvBalance(debitor.getAvBalance()-transactionVO.getAmount());
				
			
			Optional<PayeeInfo> payeeInfo=payeeInfoRepository.findById(transactionVO.getPayeeId());
			Optional<CustomerAccountInfo> account=accountRepository.findByAccountNumber(payeeInfo.get().getPayeeAccountNo());
			CustomerAccountInfo creditor= new CustomerAccountInfo();
			if (account.isPresent()) {
				creditor=account.get();
//				if (!"AS04".equalsIgnoreCase(creditor.getAccountStatus().getCode())) {
//					msg="Creditor do no have Active Account.";
//					return msg;
//				}
				if (!"AC001".equalsIgnoreCase(creditor.getAccountType().getCode())) {
					msg="You don't have saving Account";
					return msg;
				}
					creditor.setAvBalance(creditor.getAvBalance()+ transactionVO.getAmount());
					
				}
			else {
				msg="Creditor do not have a valid Account No.";
				return msg;
			}
				accountRepository.save(debitor);
				accountRepository.save(creditor);
				debitor.setStatusAsOf(new Date());
				Transaction transaction= new Transaction();
				BeanUtils.copyProperties(transactionVO, transaction);
				transaction.setAmount(transactionVO.getAmount());
				transaction.setTransactionDate(new Date());
				transaction.setDescription(transactionVO.getDescription());
				transaction.setDebitAccountNumber(transactionVO.getDebitAccountNumber());
				transaction.setPayeeId(payeeInfo.get());
				transactionReository.save(transaction);
				msg="Amount as been Successfully Transfer";
			}
		return msg;
	}

	@Override
	public List<TransactionVO> viewStatement(String customerId) {
		List<TransactionVO>transactionVOs=new ArrayList<>();
		Customer customer = customerRepository.findByEmail(customerId).get();
		Optional<CustomerAccountInfo>acnt=accountRepository.findByCustomerId(customer.getLogin());
		CustomerAccountInfo debitor= acnt.get();
		List<Transaction> transactions=transactionReository.findByDebitAccountNumber(debitor.getAccountNumber());
		for(Transaction tr: transactions) {
			TransactionVO vo=new TransactionVO();
			vo.setAmount(tr.getAmount());
			vo.setTransactionDate(tr.getTransactionDate());
			vo.setDescription(tr.getDescription());
			if (!tr.getDebitAccountNumber().equalsIgnoreCase(debitor.getAccountNumber())) {
				vo.setTransactionType("Creditor");				
			}else {
				vo.setTransactionType("Debitor");
			}
			PayeeInfoVO payeeInfoVO= new PayeeInfoVO();
			BeanUtils.copyProperties(tr.getPayeeId(), payeeInfoVO);
			vo.setpInfoVO(payeeInfoVO);
			transactionVOs.add(vo);
		}
		return transactionVOs;
	}

}
	

