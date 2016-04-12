package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.Lending;
import io.bookster.service.LendingService;
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
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Lending.
 */
@RestController
@RequestMapping("/api")
public class LendingResource {

    private final Logger log = LoggerFactory.getLogger(LendingResource.class);
        
    @Inject
    private LendingService lendingService;
    
    /**
     * POST  /lendings : Create a new lending.
     *
     * @param lending the lending to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lending, or with status 400 (Bad Request) if the lending has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lendings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lending> createLending(@Valid @RequestBody Lending lending) throws URISyntaxException {
        log.debug("REST request to save Lending : {}", lending);
        if (lending.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lending", "idexists", "A new lending cannot already have an ID")).body(null);
        }
        Lending result = lendingService.save(lending);
        return ResponseEntity.created(new URI("/api/lendings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lending", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lendings : Updates an existing lending.
     *
     * @param lending the lending to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lending,
     * or with status 400 (Bad Request) if the lending is not valid,
     * or with status 500 (Internal Server Error) if the lending couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lendings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lending> updateLending(@Valid @RequestBody Lending lending) throws URISyntaxException {
        log.debug("REST request to update Lending : {}", lending);
        if (lending.getId() == null) {
            return createLending(lending);
        }
        Lending result = lendingService.save(lending);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lending", lending.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lendings : get all the lendings.
     *
     * @param pageable the pagination information
     * @param filter the filter of the request
     * @return the ResponseEntity with status 200 (OK) and the list of lendings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lendings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lending>> getAllLendings(Pageable pageable, @RequestParam(required = false) String filter)
        throws URISyntaxException {
        if ("lendingrequest-is-null".equals(filter)) {
            log.debug("REST request to get all Lendings where lendingRequest is null");
            return new ResponseEntity<>(lendingService.findAllWhereLendingRequestIsNull(),
                    HttpStatus.OK);
        }
        log.debug("REST request to get a page of Lendings");
        Page<Lending> page = lendingService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lendings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lendings/:id : get the "id" lending.
     *
     * @param id the id of the lending to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lending, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lendings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Lending> getLending(@PathVariable Long id) {
        log.debug("REST request to get Lending : {}", id);
        Lending lending = lendingService.findOne(id);
        return Optional.ofNullable(lending)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lendings/:id : delete the "id" lending.
     *
     * @param id the id of the lending to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lendings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLending(@PathVariable Long id) {
        log.debug("REST request to delete Lending : {}", id);
        lendingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lending", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lendings?query=:query : search for the lending corresponding
     * to the query.
     *
     * @param query the query of the lending search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lendings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Lending>> searchLendings(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Lendings for query {}", query);
        Page<Lending> page = lendingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/lendings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
