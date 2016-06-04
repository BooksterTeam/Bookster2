package io.bookster.service;

import io.bookster.domain.BooksterUser;
import io.bookster.domain.User;
import io.bookster.repository.BooksterUserRepository;
import io.bookster.repository.search.BooksterUserSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing BooksterUser.
 */
@Service
@Transactional
public class BooksterUserService {

    private final Logger log = LoggerFactory.getLogger(BooksterUserService.class);
    
    @Inject
    private BooksterUserRepository booksterUserRepository;
    
    @Inject
    private BooksterUserSearchRepository booksterUserSearchRepository;
    
    /**
     * Save a booksterUser.
     * 
     * @param booksterUser the entity to save
     * @return the persisted entity
     */
    public BooksterUser save(BooksterUser booksterUser) {
        log.debug("Request to save BooksterUser : {}", booksterUser);
        BooksterUser result = booksterUserRepository.save(booksterUser);
        booksterUserSearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the booksterUsers.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<BooksterUser> findAll(Pageable pageable) {
        log.debug("Request to get all BooksterUsers");
        Page<BooksterUser> result = booksterUserRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one booksterUser by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public BooksterUser findOne(Long id) {
        log.debug("Request to get BooksterUser : {}", id);
        BooksterUser booksterUser = booksterUserRepository.findOne(id);
        return booksterUser;
    }

    /**
     *  Delete the  booksterUser by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete BooksterUser : {}", id);
        booksterUserRepository.delete(id);
        booksterUserSearchRepository.delete(id);
    }

    /**
     * Search for the booksterUser corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BooksterUser> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of BooksterUsers for query {}", query);
        return booksterUserSearchRepository.search(queryStringQuery(query), pageable);
    }

    @Transactional(readOnly = true)
    public Optional<BooksterUser> findByUser(User user){
        List<BooksterUser> booksterUsers = booksterUserRepository.findAll();
        Optional<BooksterUser> booksterUser = booksterUsers.stream().filter(findBooks -> findBooks.getUser().getId().equals(user.getId())).findAny();
        return booksterUser;
    }
}
