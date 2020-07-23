package com.rab3tech.customer.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.LoginService;
import com.rab3tech.payeeInfo.service.impl.PayeeInfoService;
import com.rab3tech.vo.ApplicationResponseVO;
import com.rab3tech.vo.LoginRequestVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerController {
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private PayeeInfoService payeeInfoService;
	
	@PostMapping("/user/login")
	public ApplicationResponseVO authUser(@RequestBody LoginRequestVO loginRequestVO) {
		Optional<LoginVO>  optional=loginService.findUserByUsername(loginRequestVO.getUsername());
		ApplicationResponseVO applicationResponseVO=new ApplicationResponseVO();
		if(optional.isPresent()) {
			applicationResponseVO.setCode(200);
			applicationResponseVO.setStatus("success");
			applicationResponseVO.setMessage("Userid is correct");
		}else {
			applicationResponseVO.setCode(400);
			applicationResponseVO.setStatus("fail");
			applicationResponseVO.setMessage("Userid is not correct");
		}
		return applicationResponseVO;
	}
	
	@GetMapping("/customer/payeeInfo/{payeeInfoId}")
	public PayeeInfoVO payeeInfoVO(@PathVariable int payeeInfoId) {
		PayeeInfoVO pInfoVO=new PayeeInfoVO();
		pInfoVO=payeeInfoService.findPayeeInfoById(payeeInfoId);
		return pInfoVO;
	}
	
	@GetMapping("/customer/deletePayee/{payeeInfoId}")
	public ApplicationResponseVO deletePayee(@PathVariable int payeeInfoId) {
		ApplicationResponseVO applicationVO =new ApplicationResponseVO();
		
		try {
			payeeInfoService.deletePayee(payeeInfoId);
			applicationVO.setCode(200);
			applicationVO.setStatus("Successful");
			applicationVO.setMessage("Payee has been successfully Deleted");
		}catch (Exception e) {
			applicationVO.setCode(400);
			applicationVO.setStatus("Error");
			applicationVO.setMessage("Error occurred, deleting the Payee");
		}
		return applicationVO;
	}
	
	@PostMapping("customer/editPayee")
	public ApplicationResponseVO editPayee(@RequestBody PayeeInfoVO payeeInfoVO) {
		ApplicationResponseVO appVO = new ApplicationResponseVO();
		try {
		payeeInfoService.editPayee(payeeInfoVO);
		appVO.setCode(200);
		appVO.setStatus("Successful");
		appVO.setMessage("Payee has been successfully Updated");
	}catch (Exception e) {
		e.printStackTrace();
		appVO.setCode(400);
		appVO.setStatus("Error");
		appVO.setMessage("Error occurred, while updating the Payee");
	}
		return appVO;
	}
}
