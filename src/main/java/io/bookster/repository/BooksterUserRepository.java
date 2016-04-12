package io.bookster.repository;

import io.bookster.domain.BooksterUser;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BooksterUser entity.
 */
public interface BooksterUserRepository extends JpaRepository<BooksterUser,Long> {

}
