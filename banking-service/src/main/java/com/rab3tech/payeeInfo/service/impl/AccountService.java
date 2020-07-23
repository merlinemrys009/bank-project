package com.rab3tech.payeeInfo.service.impl;

import java.util.Optional;

import com.rab3tech.vo.CustomerAccountInfoVO;

public interface AccountService {

	Optional<CustomerAccountInfoVO> findByUser(String customerId);

}
