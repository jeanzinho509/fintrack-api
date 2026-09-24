package com.jeanestime.fintrack.repository;

import com.jeanestime.fintrack.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {

    List<Account> findAllByUser_IdOrderByCreatedAtAsc(Long userId);
}