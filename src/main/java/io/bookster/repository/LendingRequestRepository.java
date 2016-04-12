package io.bookster.repository;

import io.bookster.domain.LendingRequest;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the LendingRequest entity.
 */
public interface LendingRequestRepository extends JpaRepository<LendingRequest,Long> {

}
