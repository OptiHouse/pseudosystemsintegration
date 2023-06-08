package com.pseudoorganization.pseudosystemsintegration.repository;

import com.pseudoorganization.pseudosystemsintegration.models.State;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StateRepository extends JpaRepository<State, UUID> {
}