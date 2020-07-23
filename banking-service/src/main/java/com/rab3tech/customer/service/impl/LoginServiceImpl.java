package com.rab3tech.customer.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.customer.service.LoginService;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.vo.ChangePasswordVO;
import com.rab3tech.vo.LoginVO;


@Service
@Transactional
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private LoginRepository loginRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public boolean checkPasswordValid(String username,String password) {
		Login  login=loginRepository.findByLoginid(username).get();
		if(bCryptPasswordEncoder.matches(password, login.getPassword())) {
			return true;
		}else {
			return false;
		}
	}
	
	
	@Override
	public void changePassword(ChangePasswordVO changePasswordVO) {
		String encodedPassword=bCryptPasswordEncoder.encode(changePasswordVO.getNewPassword());
		Login  login=loginRepository.findByLoginid(changePasswordVO.getLoginid()).get();
		login.setPassword(encodedPassword);
		login.setLlt(new Timestamp(new Date().getTime()));
		loginRepository.save(login);
	}
	
	@Override
	public Optional<LoginVO> findUserByUsername(String loginid) {
		LoginVO loginVO =new LoginVO();
		loginVO.setUsername(loginid);
		Optional<Login>  optional=loginRepository.findByLoginid(loginid);
		if(optional.isPresent()) {
			Login login=optional.get();
			loginVO.setEmail(login.getEmail());
			loginVO.setUsername(login.getLoginid());
			loginVO.setPassword(login.getPassword());
			loginVO.setLlt(login.getLlt());
			Set<Role> rolesSet=login.getRoles();
			List<String> roleList=new ArrayList<>();
			//List<String> roles= rolesSet.stream().map(Role::getName).collect(Collectors.toList());
			for(Role role:rolesSet) {
				roleList.add(role.getName());
			}
			loginVO.setRoles(roleList);
			return Optional.of(loginVO);
		}else {
			return Optional.empty();
		}
	}
	
	
	@Override
	public Optional<LoginVO> authUser(LoginVO loginVO) {
		Optional<Login>  optional=loginRepository.findByLoginidAndPassword(loginVO.getUsername(), loginVO.getPassword());
		if(optional.isPresent()) {
			Login login=optional.get();
			loginVO.setEmail(login.getEmail());
			Set<Role> rolesSet=login.getRoles();
			List<String> roleList=new ArrayList<>();
			//List<String> roles= rolesSet.stream().map(Role::getName).collect(Collectors.toList());
			for(Role role:rolesSet) {
				roleList.add(role.getName());
			}
			loginVO.setRoles(roleList);
			return Optional.of(loginVO);
		}else {
			return Optional.empty();
		}
	}
}
