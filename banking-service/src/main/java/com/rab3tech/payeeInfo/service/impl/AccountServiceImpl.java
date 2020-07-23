package com.rab3tech.payeeInfo.service.impl;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.AccountRepository;
import com.rab3tech.customer.dao.repository.CustomerRepository;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.vo.CustomerAccountInfoVO;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	CustomerRepository customerRepository;

	@Override
	public Optional<CustomerAccountInfoVO> findByUser(String customerId) {
		Customer cus=customerRepository.findByEmail(customerId).get();
		Optional<CustomerAccountInfo> cAccount=accountRepository.findByCustomerId(cus.getLogin());
		CustomerAccountInfoVO customerAccountInfoVO=new CustomerAccountInfoVO();
		if (cAccount.isPresent()) {
			BeanUtils.copyProperties(cAccount.get(), customerAccountInfoVO, new String[] {"customerId","accountType"});
			customerAccountInfoVO.setAccountType1(cAccount.get().getAccountType()!=null?cAccount.get().getAccountType().getCode():null);
			return Optional.of(customerAccountInfoVO);
		}else {
			return Optional.empty();
		}
		
	}

}
