package io.bookster.repository;

import io.bookster.domain.Lending;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Lending entity.
 */
public interface LendingRepository extends JpaRepository<Lending,Long> {

}
