package com.rab3tech.customer.dao.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rab3tech.dao.entity.Address;
import com.rab3tech.dao.entity.Login;

public interface AddressRepository extends JpaRepository<Address, Integer> {

	Optional<Address> findByAddress1(String address1);

	Optional<Address> findByLoginId(Login login);




}
