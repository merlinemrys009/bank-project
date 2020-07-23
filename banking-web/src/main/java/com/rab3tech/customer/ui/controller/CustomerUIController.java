package com.rab3tech.customer.ui.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rab3tech.customer.service.CustomerService;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.customer.service.impl.CustomerEnquiryService;
import com.rab3tech.customer.service.impl.SecurityQuestionService;
import com.rab3tech.email.service.EmailService;
import com.rab3tech.payeeInfo.service.impl.PayeeInfoService;
import com.rab3tech.request.service.impl.CheckBookRequestService;
import com.rab3tech.vo.AddressVO;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.CustomerSavingVO;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.CustomerVO;
import com.rab3tech.vo.EmailVO;
import com.rab3tech.vo.LoginVO;
import com.rab3tech.vo.PayeeInfoVO;
import com.rab3tech.vo.SecurityQuestionsVO;
import com.rab3tech.vo.TransactionVO;


@Controller
public class CustomerUIController {

	private static final Logger logger = LoggerFactory.getLogger(CustomerUIController.class);

	@Autowired
	private CustomerEnquiryService customerEnquiryService;

	
	@Autowired
	private SecurityQuestionService securityQuestionService;
	
	
	@Autowired
	private CustomerService customerService;

	@Autowired
	private EmailService emailService;
	
	@Autowired
   private LoginService loginService;
	
	@Autowired
	private PayeeInfoService payeeInfoService;
	
	@Autowired
	private CheckBookRequestService checkBookRequestService;
	

	@GetMapping("/customer/account/customerDetails")
	public String customerDetail(HttpSession session, Model model) {
		LoginVO loginVO2 = (LoginVO) session.getAttribute("userSessionVO");
		if (loginVO2 != null) {
			CustomerVO customerVO = customerService.findByEmail(loginVO2.getUsername());
			model.addAttribute("customerVO", customerVO);
			List<SecurityQuestionsVO> qstAnswerVO=securityQuestionService.findAll();
			model.addAttribute("questionVOs", qstAnswerVO);
		} else {
			model.addAttribute("error", "Please Login First");
			return "customer/login";
		}
		return "customer/customerDetail";
	}

	@PostMapping("/customer/account/customerDetailsUpdate")
	public String updateAccount(@ModelAttribute CustomerVO customerVO, Model model, HttpSession session) {
		logger.debug(customerVO.toString());
		 LoginVO loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		 if (loginVO2!=null) {
		customerService.updateCustomer(customerVO);
		List<SecurityQuestionsVO> qstAnswerVO=securityQuestionService.findAll();
		model.addAttribute("questionVOs", qstAnswerVO);
		model.addAttribute("msg", "Account has been successfully updated");
		 }
		return "redirect:/customer/account/customerDetails";
	}
	
	@PostMapping("/customer/changePassword")
	public String saveCustomerQuestions(@ModelAttribute ChangePasswordVO changePasswordVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		changePasswordVO.setLoginid(loginid);
		String viewName ="customer/dashboard";
		boolean status=loginService.checkPasswordValid(loginid,changePasswordVO.getCurrentPassword());
		if(status) {
			if(changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
				 viewName ="customer/dashboard";
				 loginService.changePassword(changePasswordVO);
			}else {
				model.addAttribute("error","Sorry , your new password and confirm passwords are not same!");
				return "customer/login";	//login.html	
			}
		}else {
			model.addAttribute("error","Sorry , your username and password are not valid!");
			return "customer/login";	//login.html	
		}
		return viewName;
	}
	
	@PostMapping("/customer/securityQuestion")
	public String saveCustomerQuestions(@ModelAttribute("customerSecurityQueAnsVO") CustomerSecurityQueAnsVO customerSecurityQueAnsVO, Model model,HttpSession session) {
		LoginVO  loginVO2=(LoginVO)session.getAttribute("userSessionVO");
		String loginid=loginVO2.getUsername();
		customerSecurityQueAnsVO.setLoginid(loginid);
		securityQuestionService.save(customerSecurityQueAnsVO);
		//
		return "customer/chagePassword";
	}

	// http://localhost:444/customer/account/registration?cuid=1585a34b5277-dab2-475a-b7b4-042e032e8121603186515
	@GetMapping("/customer/account/registration")
	public String showCustomerRegistrationPage(@RequestParam String cuid, Model model) {

		logger.debug("cuid = " + cuid);
		Optional<CustomerSavingVO> optional = customerEnquiryService.findCustomerEnquiryByUuid(cuid);
		CustomerVO customerVO = new CustomerVO();

		if (!optional.isPresent()) {
			return "customer/error";
		} else {
			// model is used to carry data from controller to the view =- JSP/
			CustomerSavingVO customerSavingVO = optional.get();
			customerVO.setEmail(customerSavingVO.getEmail());
			customerVO.setName(customerSavingVO.getName());
			customerVO.setMobile(customerSavingVO.getMobile());
			customerVO.setAddress(customerSavingVO.getLocation());
			customerVO.setToken(cuid);
			logger.debug(customerSavingVO.toString());
			// model - is hash map which is used to carry data from controller to thyme
			// leaf!!!!!
			// model is similar to request scope in jsp and servlet
			model.addAttribute("customerVO", customerVO);
			return "customer/customerRegistration"; // thyme leaf
		}
	}

	@PostMapping("/customer/account/registration")
	public String createCustomer(@ModelAttribute CustomerVO customerVO, Model model) {
		// saving customer into database
		logger.debug(customerVO.toString());
		customerVO = customerService.createAccount(customerVO);
		// Write code to send email

		EmailVO mail = new EmailVO(customerVO.getEmail(), "javahunk2020@gmail.com",
				"Regarding Customer " + customerVO.getName() + "  userid and password", "", customerVO.getName());
		mail.setUsername(customerVO.getUserid());
		mail.setPassword(customerVO.getPassword());
		emailService.sendUsernamePasswordEmail(mail);
		System.out.println(customerVO);
		model.addAttribute("loginVO", new LoginVO());
		model.addAttribute("message", "Your account has been setup successfully , please check your email.");
		return "customer/login";
	}

	@GetMapping(value = { "/customer/account/enquiry", "/", "/mocha", "/welcome" })
	public String showCustomerEnquiryPage(Model model) {
		CustomerSavingVO customerSavingVO = new CustomerSavingVO();
		// model is map which is used to carry object from controller to view
		model.addAttribute("customerSavingVO", customerSavingVO);
		return "customer/customerEnquiry"; // customerEnquiry.html
	}

	@PostMapping("/customer/account/enquiry")
	public String submitEnquiryData(@ModelAttribute CustomerSavingVO customerSavingVO, Model model) {
		boolean status = customerEnquiryService.emailNotExist(customerSavingVO.getEmail());
		logger.info("Executing submitEnquiryData");
		if (status) {
			CustomerSavingVO response = customerEnquiryService.save(customerSavingVO);
			logger.debug("Hey Customer , your enquiry form has been submitted successfully!!! and appref "
					+ response.getAppref());
			model.addAttribute("message",
					"Hey Customer , your enquiry form has been submitted successfully!!! and appref "
							+ response.getAppref());
		} else {
			model.addAttribute("message", "Sorry , this email is already in use " + customerSavingVO.getEmail());
		}
		return "customer/success"; // customerEnquiry.html
	}
	///forgot password///
	@GetMapping("/customer/account/customerPasswordRec")
	public String passwordRecoveryPage() {
		return "customer/customerPasswordRecovery";
	} 
	///Post 1
	@PostMapping("/customer/account/securityQuestion")
	public String securityQue(@RequestParam String email, Model model) {
		Optional<LoginVO> loginVOs=loginService.findUserByUsername(email);
		if (loginVOs.isPresent()) {
			CustomerSecurityQueAnsVO cAnsVO = securityQuestionService.getSecurityDetailsForUser(email);
			cAnsVO.setLoginid(email);
			model.addAttribute("question" , cAnsVO);
		}else {
			model.addAttribute("error", "Username is Invalid");
			return "customer/customerPasswordRecovery";
		}
		return "customer/securityQueShow";
	}
	///Post 2
	@PostMapping("/customer/account/validateSecurityQuestions")
	public String validateQuestion(@ModelAttribute CustomerSecurityQueAnsVO customerSecurityQueAnsVO,HttpSession session, Model model) {
		Optional<LoginVO> loginVOs =loginService.findUserByUsername(customerSecurityQueAnsVO.getLoginid());
		if (loginVOs.isPresent()) {
			boolean reslut = false;
			 reslut = securityQuestionService.validateSecurityQuestion(customerSecurityQueAnsVO);
			if (reslut) {
				model.addAttribute("loginid", loginVOs.get().getUsername());
				return "customer/updateRecoverPassword"; 
			}else {
				model.addAttribute("message","Your record does not match in System");
				model.addAttribute("loginid", loginVOs.get().getUsername());
				return "customer/securityQueShow";	
			}
		}else {
				model.addAttribute("error","Username is Invalid");
				return "redirect:/customer/account/securityQuestion";
		}
		
	}
	///Post 3
	@PostMapping("/customer/account/customerPasswordRecovered")
	public String customerPasswordRecovery(@ModelAttribute ChangePasswordVO changePasswordVO, Model model) {
		Optional<LoginVO> loginVOs = loginService.findUserByUsername(changePasswordVO.getLoginid());
		if (loginVOs.isPresent()) {
//			changePasswordVO.getCurrentPassword();
			if (changePasswordVO.getNewPassword().equals(changePasswordVO.getConfirmPassword())) {
				loginService.changePassword(changePasswordVO);
				model.addAttribute("message", "Hello!"+changePasswordVO.getLoginid()
				+"your Password has been successfully updated.");
				return "customer/login";
			}else {
				model.addAttribute("customerVO", changePasswordVO);
				return "customer/updateRecoverPassword";
			}	
			}else {
				model.addAttribute("error", "Invalid Username");
				return "redirect:/customer/account/customerPasswordRecovered";
			}	
		}
	
	@GetMapping("/customer/addPayee")
	public String addPayee(HttpSession session, Model model) {
		LoginVO loginVOs=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVOs!=null) {
			return "customer/addPayee";
		}else {
			model.addAttribute("error", "Please Login to further Process");
			return "customer/login";
		}
		
	} 
	
	@PostMapping("/customer/updatePayee")
	public String updatePayee(@ModelAttribute PayeeInfoVO payeeInfoVO, Model model, HttpSession session ) {
		LoginVO loginVOs=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVOs!=null) {
			payeeInfoVO.setCustomerId(loginVOs.getUsername());
			String message=payeeInfoService.savePayeeInfo(payeeInfoVO);
			model.addAttribute("message",message);
			return "customer/dashboard";
		}else {
			model.addAttribute("error", "Please Login to further Process");
			return "customer/login";			
		}
	}
	
	@GetMapping("/customer/payeeDataInfo")
	public String payeeData(HttpSession session, Model model, PayeeInfoVO payeeInfoVO) {
		LoginVO loginVOs=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVOs!=null) {
			payeeInfoVO.setCustomerId(loginVOs.getUsername());
			List<PayeeInfoVO> payeeInfo=payeeInfoService.findAllData(payeeInfoVO.getCustomerId());
			model.addAttribute("payeeInfoList", payeeInfo);
			return "customer/payeeData";
		}else {
			model.addAttribute("error", "Please Login to proceed further");
			return "customer/login";
		}
		
	}
	
	@GetMapping("/customer/account/moneyTransfer")
	public String fundTransfer(HttpSession session, Model model,PayeeInfoVO payeeInfoVO ) {
		LoginVO loginVO= (LoginVO)session.getAttribute("userSessionVO");
		if (loginVO!=null) {
			payeeInfoVO.setCustomerId(loginVO.getUsername());
			List<PayeeInfoVO> vos2=payeeInfoService.findData(payeeInfoVO.getCustomerId());
			model.addAttribute("payeeInfoList", vos2);
			return "customer/moneyTransfer";
		}else {
			model.addAttribute("error","Please Login to Proceed Further.");
			return "customer/login";
		}
	}
	
	@PostMapping("/customer/account/moneyTransfered")
	public String fundTransfer(@ModelAttribute TransactionVO transactionVO, HttpSession session, Model model) {
		LoginVO loginVO=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVO!=null) {
			transactionVO.setCustomerId(loginVO.getUsername());
			String msg=payeeInfoService.transferOfFunds(transactionVO);
			model.addAttribute("message",msg);
			return "customer/dashboard";
		}else {
			model.addAttribute("error","Please Login in to Proceed Further.");
			return "customer/login";
		}
	}
	
	@GetMapping("/customer/transactionStatement")
	public String viewStatment(@ModelAttribute TransactionVO transactionVO,HttpSession session, Model model) {
		LoginVO loginVO=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVO!=null) {
			transactionVO.setCustomerId(loginVO.getUsername());	
			List<TransactionVO> vos=payeeInfoService.viewStatement(transactionVO.getCustomerId());
			model.addAttribute("transactionVO",vos);
			return "customer/transactionStatement";
		}else {
			model.addAttribute("error","Please Login to Preceed Further.");
			return "customer/login";
		}
	}
	
	///////Request Check Book/////
	@GetMapping("/customer/account/requestCheckBook")
	public String request(@ModelAttribute AddressVO addressVO, HttpSession session, Model model) {
		LoginVO loginVO=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVO!=null) {
			addressVO.setLoginId(loginVO.getUsername());
			AddressVO vo=checkBookRequestService.findUserById(addressVO);
			if (vo.getAddress1()!=null) {
				model.addAttribute("addressVO", vo);
				return "customer/requestView";
			}else {
				model.addAttribute("addressVO", vo);
				return "customer/request";
			}
			
		}else {
			model.addAttribute("error","Please Login to Proceed Further.");
			return "customer/login";
		}
		
	}
	
	@PostMapping("/customer/account/addressUpdated")
	public String updateAddress(@ModelAttribute AddressVO addressVO, HttpSession session, Model model) {
		LoginVO loginVO=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVO!=null) {
			addressVO.setLoginId(loginVO.getUsername());
			String message=checkBookRequestService.update(addressVO);
			model.addAttribute("message",message);
		}
		return "customer/dashboard";
		
	}
	
	@GetMapping("/customer/account/requestView")
	public String requestView(@ModelAttribute AddressVO addressVO, HttpSession session, Model model) {
		LoginVO loginVO=(LoginVO)session.getAttribute("userSessionVO");
		if (loginVO!=null) {
			addressVO.setLoginId(loginVO.getUsername());
			if (addressVO.getAddress1()!=null) {
				model.addAttribute("addressVO", addressVO);
				return "customer/requestView";
		}
		
	}
		return "customer/login";
}
}

