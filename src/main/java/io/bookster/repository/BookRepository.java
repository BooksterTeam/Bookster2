package io.bookster.repository;

import io.bookster.domain.Book;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Book entity.
 */
public interface BookRepository extends JpaRepository<Book,Long> {

    @Query("select distinct book from Book book left join fetch book.bookAuthors")
    List<Book> findAllWithEagerRelationships();

    @Query("select book from Book book left join fetch book.bookAuthors where book.id =:id")
    Book findOneWithEagerRelationships(@Param("id") Long id);

}
