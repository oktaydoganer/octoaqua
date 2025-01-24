package com.nexmind3.octoaqua.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<T, L extends Number> extends JpaRepository<T, Long> {

}
