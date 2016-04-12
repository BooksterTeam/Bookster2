package io.bookster.repository;

import io.bookster.domain.Copy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Copy entity.
 */
public interface CopyRepository extends JpaRepository<Copy,Long> {

}
