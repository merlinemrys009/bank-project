package com.rab3tech.customer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.service.exception.BankServiceException;
import com.rab3tech.vo.CustomerSavingVO;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/v3")
public class CustomerAccountEnquiryController {

	@Autowired
	private CustomerEnquiryService customerEnquiryService;
	
	@PostMapping("/customers/enquiry")
	public CustomerSavingVO saveEnquiry(@RequestBody CustomerSavingVO customerSavingVO) {
		//write code for email validation;
		CustomerSavingVO  response=null;
	    boolean status=customerEnquiryService.emailNotExist(customerSavingVO.getEmail());
		if(status) {
		     response=customerEnquiryService.save(customerSavingVO);
		}else {
		  throw new BankServiceException("Sorry , this email is already in use "+customerSavingVO.getEmail());
		}
		return response;
	}
	
	@GetMapping("/customers/enquiry")
	public List<CustomerSavingVO> getAllEnquiry() {
		List<CustomerSavingVO>  responses=customerEnquiryService.findAll();
		return responses;
	}
	
	@GetMapping("/customers/enquiry/pending")
	public List<CustomerSavingVO> getAllPendingEnquiry() {
		List<CustomerSavingVO>  responses=customerEnquiryService.findPendingEnquiry();
		return responses;
	}
	
	@GetMapping(value="/customers/enquiry/{csaid}")
	public CustomerSavingVO getEnquiry(@PathVariable int csaid) {
		CustomerSavingVO  response=customerEnquiryService.findById(csaid);
		return response;
	}
	
}
