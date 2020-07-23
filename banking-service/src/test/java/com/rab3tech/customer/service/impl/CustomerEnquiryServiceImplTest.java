package com.rab3tech.customer.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.admin.dao.repository.AccountTypeRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.vo.CustomerSavingVO;


@RunWith(MockitoJUnitRunner.class)
public class CustomerEnquiryServiceImplTest {

	@Mock
	private CustomerAccountEnquiryRepository customerAccountEnquiryRepository;
	
	@Mock
	private AccountTypeRepository accountTypeRepository;
	
	@Mock
	
	private AccountStatusRepository accountStatusRepository;
	
	@Mock
	private EmailService emailService;
	
	@InjectMocks
	private CustomerEnquiryServiceImpl customerEnquiryServiceImpl;
	
	@Before
	public void init() {
		 MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void testFindPendingEnquiryWhenResult() {
		 List<CustomerSaving> customerSavings=new ArrayList<>();
		 CustomerSaving customerSaving1=new CustomerSaving(122,"nagendra","nagen@gmail.com","02390","NA","92828ns8w3",null,null,null,"A435");
		 CustomerSaving customerSaving2=new CustomerSaving(133,"moshi","moshi@gmail.com","345435","NA","346456",null,null,null,"A0393");
		 customerSavings.add(customerSaving1);
		 customerSavings.add(customerSaving2);
		 when(customerAccountEnquiryRepository.findPendingEnquiries(AccountStatusEnum.PENDING.name())).thenReturn(customerSavings);
		 List<CustomerSavingVO> customerSavingVOs=customerEnquiryServiceImpl.findPendingEnquiry();
		 assertNotNull(customerSavingVOs);
		 assertEquals(customerSavingVOs.size(),2);
		 assertEquals(customerSavingVOs.get(0).getName(),"nagendra");
		 assertEquals(customerSavingVOs.get(0).getEmail(),"nagen@gmail.com");
		 assertEquals(customerSavingVOs.get(1).getName(),"moshi");
		 assertEquals(customerSavingVOs.get(1).getEmail(),"moshi@gmail.com");
		 verify(customerAccountEnquiryRepository, times(1)).findPendingEnquiries(AccountStatusEnum.PENDING.name());
	     verifyNoMoreInteractions(customerAccountEnquiryRepository);
	}
	
	@Test
	public void testFindPendingEnquiryWhenNoResult() {
		 List<CustomerSaving> customerSavings=new ArrayList<>();
		 when(customerAccountEnquiryRepository.findPendingEnquiries(AccountStatusEnum.PENDING.name())).thenReturn(customerSavings);
		 List<CustomerSavingVO> customerSavingVOs=customerEnquiryServiceImpl.findPendingEnquiry();
		 assertNotNull(customerSavingVOs);
		 assertEquals(customerSavingVOs.size(),0);
		 verify(customerAccountEnquiryRepository, times(1)).findPendingEnquiries(AccountStatusEnum.PENDING.name());
	     verifyNoMoreInteractions(customerAccountEnquiryRepository);
	}


	@Test
	public void testEmailNotExistFalse() {
			CustomerSaving customerSaving=new CustomerSaving(122,"Cubic","cubic@gmail.com","02390","NA","92828ns8w3",null,null,null,"A435");
			Optional<CustomerSaving> optional=Optional.of(customerSaving);
			when(customerAccountEnquiryRepository.findByEmail("cubic@gmail.com")).thenReturn(optional);
			boolean result=customerEnquiryServiceImpl.emailNotExist("cubic@gmail.com");
			
			assertFalse(result);
	}
	
   @Test
	public void testEmailNotExistTrue() {
			Optional<CustomerSaving> optional=Optional.empty();
			when(customerAccountEnquiryRepository.findByEmail("cubic@gmail.com")).thenReturn(optional);
			boolean result=customerEnquiryServiceImpl.emailNotExist("cubic@gmail.com");
			assertTrue(result);
	   	    verify(customerAccountEnquiryRepository, times(1)).findByEmail("cubic@gmail.com");
		   verifyNoMoreInteractions(customerAccountEnquiryRepository);
	}

	@Test
	@Ignore
	public void testUpdateEnquiryRegId() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFindById() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testChangeEnquiryStatus() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFindCustomerEnquiryByUuid() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDeleteById() {
		fail("Not yet implemented");
	}

}
