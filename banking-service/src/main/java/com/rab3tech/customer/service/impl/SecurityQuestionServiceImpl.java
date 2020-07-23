package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.CustomerQuestionsAnsRepository;
import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.dao.repository.SecurityQuestionsRepository;
import com.rab3tech.dao.entity.CustomerQuestionAnswer;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.SecurityQuestions;
import com.rab3tech.vo.CustomerSecurityQueAnsVO;
import com.rab3tech.vo.SecurityQuestionsVO;

@Transactional
@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
	
	private static final Logger logger = LoggerFactory.getLogger(SecurityQuestionServiceImpl.class);//ya i know

	@Autowired
	private SecurityQuestionsRepository questionsRepository;

	@Autowired
	private LoginRepository loginRepository;

	@Autowired
	private CustomerQuestionsAnsRepository customerQuestionsAnsRepository;

	@Override
	public void save(CustomerSecurityQueAnsVO customerSecurityQueAnsVO) {

		// Deleting all customer questions for existing user
		List<CustomerQuestionAnswer> customerQuestionAnswers = customerQuestionsAnsRepository
				.findQuestionAnswer(customerSecurityQueAnsVO.getLoginid());
		if (customerQuestionAnswers.size() > 0)
			customerQuestionsAnsRepository.deleteAll(customerQuestionAnswers);

		CustomerQuestionAnswer customerQuestionAnswer1 = new CustomerQuestionAnswer();
		Login login = loginRepository.findById(customerSecurityQueAnsVO.getLoginid()).get();

		String quetionText = questionsRepository
				.findById(Integer.parseInt(customerSecurityQueAnsVO.getSecurityQuestion1())).get().getQuestions();
		customerQuestionAnswer1.setQuestion(quetionText);
		customerQuestionAnswer1.setAnswer(customerSecurityQueAnsVO.getSecurityQuestionAnswer1());
		customerQuestionAnswer1.setDoe(new Timestamp(new Date().getTime()));
		customerQuestionAnswer1.setDom(new Timestamp(new Date().getTime()));
		customerQuestionAnswer1.setLogin(login);
		customerQuestionsAnsRepository.save(customerQuestionAnswer1);

		CustomerQuestionAnswer customerQuestionAnswer2 = new CustomerQuestionAnswer();
		quetionText = questionsRepository.findById(Integer.parseInt(customerSecurityQueAnsVO.getSecurityQuestion2()))
				.get().getQuestions();
		customerQuestionAnswer2.setQuestion(quetionText);
		customerQuestionAnswer2.setAnswer(customerSecurityQueAnsVO.getSecurityQuestionAnswer2());
		customerQuestionAnswer2.setDoe(new Timestamp(new Date().getTime()));
		customerQuestionAnswer2.setDom(new Timestamp(new Date().getTime()));
		customerQuestionAnswer2.setLogin(login);
		customerQuestionsAnsRepository.save(customerQuestionAnswer2);

	}

	@Override
	public List<SecurityQuestionsVO> findAll() {
		List<SecurityQuestions> securityQuestions = questionsRepository.findAll();
		List<SecurityQuestionsVO> questionsVOs = new ArrayList<>();
		for (SecurityQuestions questions : securityQuestions) {
			SecurityQuestionsVO questionsVO = new SecurityQuestionsVO();
			BeanUtils.copyProperties(questions, questionsVO);
			questionsVOs.add(questionsVO);
		}
		return questionsVOs;
		/*
		 * return securityQuestions.stream().map(tt->{ SecurityQuestionsVO
		 * questionsVO=new SecurityQuestionsVO(); BeanUtils.copyProperties(tt,
		 * questionsVO); return questionsVO; }).collect(Collectors.toList());
		 */
	}

	@Override
	public CustomerSecurityQueAnsVO getSecurityDetailsForUser(String username) {
		CustomerSecurityQueAnsVO seQueAnsVO = new CustomerSecurityQueAnsVO();
		List<CustomerQuestionAnswer> cAnswer = customerQuestionsAnsRepository.findQuestionAnswer(username);
		seQueAnsVO.setSecurityQuestion1(cAnswer.get(0).getQuestion());
		seQueAnsVO.setSecurityQuestion2(cAnswer.get(1).getQuestion());
		seQueAnsVO.setLoginid(username);
		return seQueAnsVO;
	}

	@Override
	public boolean validateSecurityQuestion(CustomerSecurityQueAnsVO cSecurityQueAnsVO) {
		boolean validated=false;
		List<CustomerQuestionAnswer> cQuestionAnswers = customerQuestionsAnsRepository.findQuestionAnswer(cSecurityQueAnsVO.getLoginid());
		logger.debug(String.valueOf(validated));
		if (cQuestionAnswers.get(0).getQuestion().equalsIgnoreCase(cSecurityQueAnsVO.getSecurityQuestion1())
				&& cQuestionAnswers.get(0).getAnswer().equalsIgnoreCase(cSecurityQueAnsVO.getSecurityQuestionAnswer1()) && (cQuestionAnswers.get(1).getQuestion().equalsIgnoreCase(cSecurityQueAnsVO.getSecurityQuestion2())
						&& cQuestionAnswers.get(1).getAnswer().equalsIgnoreCase(cSecurityQueAnsVO.getSecurityQuestionAnswer2()))) {
			validated = true;
			logger.debug(String.valueOf(validated));//bro i am not lying
		}else {
			validated = false;
		}
		logger.debug(String.valueOf(validated));
		return validated;
	}
	
//	String q1DB=cQuestionAnswers.get(0).getQuestion();
//	String q2DB=cQuestionAnswers.get(1).getQuestion();
//	String a1DB=cQuestionAnswers.get(0).getAnswer();
//	String a2DB=cQuestionAnswers.get(1).getAnswer();
//	
//	String q1Web=cSecurityQueAnsVO.getSecurityQuestion1();
//	String q2Web=cSecurityQueAnsVO.getSecurityQuestion2();
//	String a1Web=cSecurityQueAnsVO.getSecurityQuestionAnswer1();
//	String a2Web=cSecurityQueAnsVO.getSecurityQuestionAnswer2();

}
