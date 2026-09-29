package com.jeanestime.fintrack.repository;

import com.jeanestime.fintrack.entity.FinancialTransaction;
import com.jeanestime.fintrack.entity.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface FinancialTransactionRepository
        extends JpaRepository<FinancialTransaction, Long> {

    List<FinancialTransaction>
            findAllByAccount_IdOrderByOccurredAtDesc(Long accountId);

    @Query("""
            SELECT COALESCE(SUM(t.amount), 0)
            FROM FinancialTransaction t
            WHERE t.account.id = :accountId
              AND t.type = :type
            """)
    BigDecimal sumAmountByAccountIdAndType(
            @Param("accountId") Long accountId,
            @Param("type") TransactionType type
    );
}