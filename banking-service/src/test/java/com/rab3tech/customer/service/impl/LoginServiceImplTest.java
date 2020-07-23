package com.rab3tech.customer.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.rab3tech.customer.dao.repository.LoginRepository;
import com.rab3tech.dao.entity.Login;
import com.rab3tech.dao.entity.Role;
import com.rab3tech.vo.LoginVO;


@RunWith(MockitoJUnitRunner.class)
public class LoginServiceImplTest {
	
	@Mock
	private LoginRepository loginRepository;
	
	@InjectMocks
	private LoginServiceImpl loginServiceImpl;
	
	
	@Before
	public void init() {
		 MockitoAnnotations.initMocks(this); //Initializing mocking for each test cases
	}

	@Test
	public void testFindUserByUsernameWhenExist() {
		Login login=new Login();
		login.setEmail("nagen@gmail.com");
		login.setLoginid("owowo282");
		login.setPassword("cool@1234");
		Set<Role> rolesSet=new HashSet<>();
		Role role=new Role();
		role.setName("ADMIN");
		role.setRid(100);
		rolesSet.add(role);
		login.setRoles(rolesSet);
		Optional<Login> ologin=Optional.of(login);
		when(loginRepository.findByLoginid("nagen@gmail.com")).thenReturn(ologin);
		Optional<LoginVO>   optional=loginServiceImpl.findUserByUsername("nagen@gmail.com");
		assertTrue(optional.isPresent());
		assertEquals("nagen@gmail.com", optional.get().getEmail());
	}
	
	@Test
	public void testFindUserByUsernameWhenNotExist() {
		Optional<Login> ologin=Optional.empty();
		when(loginRepository.findByLoginid("nagen@gmail.com")).thenReturn(ologin);
		Optional<LoginVO>   optional=loginServiceImpl.findUserByUsername("nagen@gmail.com");
		assertFalse(optional.isPresent());
	}

	@Test
	public void testAuthUserWhenExist() {
		Login login=new Login();
		login.setEmail("nagen@gmail.com");
		login.setLoginid("owowo282");
		login.setPassword("cool@1234");
		Set<Role> rolesSet=new HashSet<>();
		Role role=new Role();
		role.setName("ADMIN");
		role.setRid(100);
		rolesSet.add(role);
		login.setRoles(rolesSet);
		Optional<Login> ologin=Optional.of(login);
		
		LoginVO loginVO=new LoginVO();
		loginVO.setUsername("nagen@gmail.com");
		loginVO.setPassword("cool@1234");
		when(loginRepository.findByLoginidAndPassword("nagen@gmail.com","cool@1234")).thenReturn(ologin);
		Optional<LoginVO> loginVO2=loginServiceImpl.authUser(loginVO);
		assertTrue(loginVO2.isPresent());
		assertEquals("cool@1234", loginVO2.get().getPassword());
		assertEquals("ADMIN", loginVO2.get().getRoles().get(0));
	}
	
	@Test
	public void testAuthUserWhenNotExist() {
		Optional<Login> ologin=Optional.empty();
		LoginVO loginVO=new LoginVO();
		loginVO.setUsername("nagen@gmail.com");
		loginVO.setPassword("cool@1234");
		when(loginRepository.findByLoginidAndPassword("nagen@gmail.com","cool@1234")).thenReturn(ologin);
		Optional<LoginVO> loginVO2=loginServiceImpl.authUser(loginVO);
		assertFalse(loginVO2.isPresent());
	}

}
