package com.rab3tech.request.service.impl;

import com.rab3tech.vo.AddressVO;

public interface CheckBookRequestService {

	AddressVO findUserById(AddressVO addressVO);

	String update(AddressVO addressVO);


}
