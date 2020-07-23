package com.rab3tech.request.service.impl;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.AddressRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.dao.entity.Address;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.AddressVO;

@Service
@Transactional
public class CheckBookRequestServiceImpl implements CheckBookRequestService {
	
	@Autowired
	AddressRepository addressRepository;
	
	@Autowired
	private LoginRepository loginRepository;

	@Override
	public AddressVO findUserById(AddressVO addressVO) {
		Login login=loginRepository.findByLoginid(addressVO.getLoginId()).get();
		Optional<Address> address=addressRepository.findByLoginId(login);
		if (address.isPresent()) {
			BeanUtils.copyProperties(address.get(), addressVO, Utils.ignoreNullData(address.get()));
		}
		return addressVO;
	}

	@Override
	public String update(AddressVO addressVO) {
		Optional<Address> address=addressRepository.findByAddress1(addressVO.getAddress1());
		Optional<Login> login=loginRepository.findByLoginid(addressVO.getLoginId());
		if (address.isPresent()) {
			
			BeanUtils.copyProperties(addressVO, address, Utils.ignoreNullData(addressVO));
			address.get().setDoM(new Date());
			addressRepository.save(address.get());
			
		}else {
			Address add=new Address();
			BeanUtils.copyProperties(addressVO, add , Utils.ignoreNullData(addressVO));
			add.setLoginId(login.get());
			add.setDoE(new Date());
			addressRepository.save(add);
		}
		return "Address successfully updated";
	}
	
	
}
