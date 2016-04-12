package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.BooksterUser;
import io.bookster.service.BooksterUserService;
import io.bookster.web.rest.util.HeaderUtil;
import io.bookster.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing BooksterUser.
 */
@RestController
@RequestMapping("/api")
public class BooksterUserResource {

    private final Logger log = LoggerFactory.getLogger(BooksterUserResource.class);
        
    @Inject
    private BooksterUserService booksterUserService;
    
    /**
     * POST  /bookster-users : Create a new booksterUser.
     *
     * @param booksterUser the booksterUser to create
     * @return the ResponseEntity with status 201 (Created) and with body the new booksterUser, or with status 400 (Bad Request) if the booksterUser has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bookster-users",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooksterUser> createBooksterUser(@RequestBody BooksterUser booksterUser) throws URISyntaxException {
        log.debug("REST request to save BooksterUser : {}", booksterUser);
        if (booksterUser.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("booksterUser", "idexists", "A new booksterUser cannot already have an ID")).body(null);
        }
        BooksterUser result = booksterUserService.save(booksterUser);
        return ResponseEntity.created(new URI("/api/bookster-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("booksterUser", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /bookster-users : Updates an existing booksterUser.
     *
     * @param booksterUser the booksterUser to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated booksterUser,
     * or with status 400 (Bad Request) if the booksterUser is not valid,
     * or with status 500 (Internal Server Error) if the booksterUser couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/bookster-users",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooksterUser> updateBooksterUser(@RequestBody BooksterUser booksterUser) throws URISyntaxException {
        log.debug("REST request to update BooksterUser : {}", booksterUser);
        if (booksterUser.getId() == null) {
            return createBooksterUser(booksterUser);
        }
        BooksterUser result = booksterUserService.save(booksterUser);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("booksterUser", booksterUser.getId().toString()))
            .body(result);
    }

    /**
     * GET  /bookster-users : get all the booksterUsers.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of booksterUsers in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/bookster-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BooksterUser>> getAllBooksterUsers(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of BooksterUsers");
        Page<BooksterUser> page = booksterUserService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/bookster-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /bookster-users/:id : get the "id" booksterUser.
     *
     * @param id the id of the booksterUser to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the booksterUser, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/bookster-users/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BooksterUser> getBooksterUser(@PathVariable Long id) {
        log.debug("REST request to get BooksterUser : {}", id);
        BooksterUser booksterUser = booksterUserService.findOne(id);
        return Optional.ofNullable(booksterUser)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /bookster-users/:id : delete the "id" booksterUser.
     *
     * @param id the id of the booksterUser to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/bookster-users/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBooksterUser(@PathVariable Long id) {
        log.debug("REST request to delete BooksterUser : {}", id);
        booksterUserService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("booksterUser", id.toString())).build();
    }

    /**
     * SEARCH  /_search/bookster-users?query=:query : search for the booksterUser corresponding
     * to the query.
     *
     * @param query the query of the booksterUser search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/bookster-users",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<BooksterUser>> searchBooksterUsers(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of BooksterUsers for query {}", query);
        Page<BooksterUser> page = booksterUserService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/bookster-users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
