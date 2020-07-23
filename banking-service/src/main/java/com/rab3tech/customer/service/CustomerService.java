package com.rab3tech.customer.service;

import java.util.Optional;

import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerVO;

public interface CustomerService {

	CustomerVO createAccount(CustomerVO customerVO);

	CustomerAccountInfoVO createBankAccount(int csaid);

	CustomerVO findByEmail(String username);

	void updateCustomer(CustomerVO customerVO);

	Optional<CustomerVO> findPasswordByEmail(String email);

}
