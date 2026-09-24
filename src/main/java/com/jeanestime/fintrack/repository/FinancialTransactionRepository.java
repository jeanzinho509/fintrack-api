package com.jeanestime.fintrack.repository;

import com.jeanestime.fintrack.entity.FinancialTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialTransactionRepository
        extends JpaRepository<FinancialTransaction, Long> {

    List<FinancialTransaction>
            findAllByAccount_IdOrderByOccurredAtDesc(Long accountId);
}