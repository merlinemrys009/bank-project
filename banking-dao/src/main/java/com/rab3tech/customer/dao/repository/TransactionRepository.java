package com.rab3tech.customer.dao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rab3tech.dao.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

	@Query("select tra from Transaction tra where tra.debitAccountNumber =:pAccountNumber or tra.payeeId.payeeAccountNo =:pAccountNumber order by tra.transactionDate desc")
	List<Transaction> findByDebitAccountNumber(@Param("pAccountNumber")String accountNumber);
	

}
