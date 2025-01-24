package com.nexmind3.octoaqua.repository;

import com.nexmind3.octoaqua.entity.Relay;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RelayRepository extends GenericRepository<Relay, Number> {

    List<Relay> getAllSorted();

    Relay findByName(String name);
}
