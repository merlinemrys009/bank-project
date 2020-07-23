package com.rab3tech.customer.service.impl;

import java.beans.PropertyDescriptor;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.admin.dao.repository.AccountStatusRepository;
import com.rab3tech.admin.dao.repository.AccountTypeRepository;
import com.rab3tech.admin.dao.repository.MagicCustomerRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountApprovedRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountEnquiryRepository;
import com.rab3tech.customer.dao.repository.CustomerAccountInfoRepository;
import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.RoleRepository;
import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.dao.entity.AccountStatus;
import com.rab3tech.dao.entity.Customer;
import com.rab3tech.dao.entity.CustomerAccountInfo;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.CustomerSaving;
import com.rab3tech.dao.entity.CustomerSavingApproved;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.utils.AccountStatusEnum;
import com.rab3tech.utils.PasswordGenerator;
import com.rab3tech.utils.Utils;
import com.rab3tech.vo.CustomerAccountInfoVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);

	@Autowired
	private SecurityQuestionService securityQuestionService;

	@Autowired
	private MagicCustomerRepository customerRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private AccountStatusRepository accountStatusRepository;

	@Autowired
	private AccountTypeRepository accountTypeRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private CustomerAccountEnquiryRepository customerAccountEnquiryRepository;

	@Autowired
	private CustomerAccountApprovedRepository customerAccountApprovedRepository;

	@Autowired
	private CustomerAccountInfoRepository customerAccountInfoRepository;

	@Autowired		
	private CustomerQuestionsAnsRepository customerQARepository;
	
	@Override
	public Optional<CustomerVO> findPasswordByEmail(String email){
		Optional<Customer> optional=customerRepository.findByEmail(email);
		CustomerVO customerVO=null;
		if (optional.isPresent()) {
			customerVO = new CustomerVO();
			BeanUtils.copyProperties(optional.get(), customerVO);
		}
		return Optional.ofNullable(customerVO);
	}

	@Override
	public CustomerVO findByEmail(String username) {
		Customer customer = customerRepository.findByEmail(username).get();
		CustomerVO customerVO = new CustomerVO();
		BeanUtils.copyProperties(customer, customerVO);
		List<CustomerQuestionAnswer> qAnswers = customerQARepository.findQuestionAnswer(username);
		customerVO.setQuestion1(qAnswers.get(0).getQuestion());
		customerVO.setQuestion2(qAnswers.get(1).getQuestion());
		customerVO.setAnswer1(qAnswers.get(0).getAnswer());
		customerVO.setAnswer2(qAnswers.get(1).getAnswer());
		return customerVO;
	}

	@Override
	public void updateCustomer(CustomerVO customerVO) {
		String methodName = "updateCustomer";
		logger.debug(methodName);
		logger.debug(customerVO.toString());
		Customer customer = customerRepository.findByEmail(customerVO.getEmail()).get();
		BeanUtils.copyProperties(customerVO, customer, ignoreNullData(customerVO));
		customerRepository.save(customer);

		List<CustomerQuestionAnswer> qAnswers = customerQARepository.findQuestionAnswer(customerVO.getEmail());
		if (qAnswers.get(0).getAnswer().equals(customerVO.getAnswer1())
				&& qAnswers.get(1).getAnswer().equals(customerVO.getAnswer2())
				&& qAnswers.get(0).getQuestion().equals(customerVO.getQuestion1())
				&& qAnswers.get(1).getQuestion().equals(customerVO.getQuestion2())) {
			logger.debug("No changes were made");
		} else {
			CustomerSecurityQueAnsVO customerSecutiryQAns = new CustomerSecurityQueAnsVO();
			customerSecutiryQAns.setLoginid(customerVO.getEmail());
			customerSecutiryQAns.setSecurityQuestion1(customerVO.getQuestion1());
			customerSecutiryQAns.setSecurityQuestion2(customerVO.getQuestion2());
			customerSecutiryQAns.setSecurityQuestionAnswer1(customerVO.getAnswer1());
			customerSecutiryQAns.setSecurityQuestionAnswer2(customerVO.getAnswer2());
			securityQuestionService.save(customerSecutiryQAns);

		}

	}

	public String[] ignoreNullData(Object sources) {
		BeanWrapper wrapper = new BeanWrapperImpl(sources);
		PropertyDescriptor[] pDescriptors = wrapper.getPropertyDescriptors();
		Set<String> nullValue = new HashSet<String>();
		for (PropertyDescriptor pd : pDescriptors) {
			Object object = wrapper.getPropertyValue(pd.getName());
			if (object == null)
				nullValue.add(pd.getName());

		}
		String[] ignoredData = new String[nullValue.size()];
		return nullValue.toArray(ignoredData);
	}

	@Override
	public CustomerAccountInfoVO createBankAccount(int csaid) {
		// logic
		String customerAccount = Utils.generateCustomerAccount();
		CustomerSaving customerSaving = customerAccountEnquiryRepository.findById(csaid).get();

		CustomerAccountInfo customerAccountInfo = new CustomerAccountInfo();
		customerAccountInfo.setAccountNumber(customerAccount);
		customerAccountInfo.setAccountType(customerSaving.getAccType());
		customerAccountInfo.setAvBalance(1000.0F);
		customerAccountInfo.setBranch(customerSaving.getLocation());
		customerAccountInfo.setCurrency("$");
		Customer customer = customerRepository.findByEmail(customerSaving.getEmail()).get();
		customerAccountInfo.setCustomerId(customer.getLogin());
		customerAccountInfo.setStatusAsOf(new Date());
		customerAccountInfo.setTavBalance(1000.0F);
		CustomerAccountInfo customerAccountInfo2 = customerAccountInfoRepository.save(customerAccountInfo);

		CustomerSavingApproved customerSavingApproved = new CustomerSavingApproved();
		BeanUtils.copyProperties(customerSaving, customerSavingApproved);
		customerSavingApproved.setAccType(customerSaving.getAccType());
		customerSavingApproved.setStatus(customerSaving.getStatus());
		// saving entity into customer_saving_enquiry_approved_tbl
		customerAccountApprovedRepository.save(customerSavingApproved);

		// delete data from
		customerAccountEnquiryRepository.delete(customerSaving);

		CustomerAccountInfoVO accountInfoVO = new CustomerAccountInfoVO();
		BeanUtils.copyProperties(customerAccountInfo2, accountInfoVO);
		return accountInfoVO;

	}

	@Override
	public CustomerVO createAccount(CustomerVO customerVO) {
		Customer pcustomer = new Customer();
		BeanUtils.copyProperties(customerVO, pcustomer);
		Login login = new Login();
		login.setNoOfAttempt(3);
		login.setLoginid(customerVO.getEmail());
		login.setName(customerVO.getName());
		String genPassword = PasswordGenerator.generateRandomPassword(8);
		customerVO.setPassword(genPassword);
		login.setPassword(bCryptPasswordEncoder.encode(genPassword));
		login.setToken(customerVO.getToken());
		login.setLocked("no");

		Role entity = roleRepository.findById(3).get();
		Set<Role> roles = new HashSet<>();
		roles.add(entity);
		// setting roles inside login
		login.setRoles(roles);
		// setting login inside
		pcustomer.setLogin(login);
		Customer dcustomer = customerRepository.save(pcustomer);
		customerVO.setId(dcustomer.getId());
		customerVO.setUserid(customerVO.getUserid());

		Optional<CustomerSaving> optional = customerAccountEnquiryRepository.findByEmail(dcustomer.getEmail());
		if (optional.isPresent()) {
			CustomerSaving customerSaving = optional.get();
			AccountStatus accountStatus = accountStatusRepository.findByCode(AccountStatusEnum.REGISTERED.getCode())
					.get();
			customerSaving.setStatus(accountStatus);
		}
		return customerVO;
	}

}
