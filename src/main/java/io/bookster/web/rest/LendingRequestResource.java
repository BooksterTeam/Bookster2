package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.LendingRequest;
import io.bookster.service.LendingRequestService;
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
 * REST controller for managing LendingRequest.
 */
@RestController
@RequestMapping("/api")
public class LendingRequestResource {

    private final Logger log = LoggerFactory.getLogger(LendingRequestResource.class);
        
    @Inject
    private LendingRequestService lendingRequestService;
    
    /**
     * POST  /lending-requests : Create a new lendingRequest.
     *
     * @param lendingRequest the lendingRequest to create
     * @return the ResponseEntity with status 201 (Created) and with body the new lendingRequest, or with status 400 (Bad Request) if the lendingRequest has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lending-requests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LendingRequest> createLendingRequest(@Valid @RequestBody LendingRequest lendingRequest) throws URISyntaxException {
        log.debug("REST request to save LendingRequest : {}", lendingRequest);
        if (lendingRequest.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("lendingRequest", "idexists", "A new lendingRequest cannot already have an ID")).body(null);
        }
        LendingRequest result = lendingRequestService.save(lendingRequest);
        return ResponseEntity.created(new URI("/api/lending-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("lendingRequest", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /lending-requests : Updates an existing lendingRequest.
     *
     * @param lendingRequest the lendingRequest to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated lendingRequest,
     * or with status 400 (Bad Request) if the lendingRequest is not valid,
     * or with status 500 (Internal Server Error) if the lendingRequest couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/lending-requests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LendingRequest> updateLendingRequest(@Valid @RequestBody LendingRequest lendingRequest) throws URISyntaxException {
        log.debug("REST request to update LendingRequest : {}", lendingRequest);
        if (lendingRequest.getId() == null) {
            return createLendingRequest(lendingRequest);
        }
        LendingRequest result = lendingRequestService.save(lendingRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("lendingRequest", lendingRequest.getId().toString()))
            .body(result);
    }

    /**
     * GET  /lending-requests : get all the lendingRequests.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of lendingRequests in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/lending-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LendingRequest>> getAllLendingRequests(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of LendingRequests");
        Page<LendingRequest> page = lendingRequestService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/lending-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /lending-requests/:id : get the "id" lendingRequest.
     *
     * @param id the id of the lendingRequest to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the lendingRequest, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/lending-requests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<LendingRequest> getLendingRequest(@PathVariable Long id) {
        log.debug("REST request to get LendingRequest : {}", id);
        LendingRequest lendingRequest = lendingRequestService.findOne(id);
        return Optional.ofNullable(lendingRequest)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /lending-requests/:id : delete the "id" lendingRequest.
     *
     * @param id the id of the lendingRequest to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/lending-requests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLendingRequest(@PathVariable Long id) {
        log.debug("REST request to delete LendingRequest : {}", id);
        lendingRequestService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("lendingRequest", id.toString())).build();
    }

    /**
     * SEARCH  /_search/lending-requests?query=:query : search for the lendingRequest corresponding
     * to the query.
     *
     * @param query the query of the lendingRequest search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/lending-requests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<LendingRequest>> searchLendingRequests(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of LendingRequests for query {}", query);
        Page<LendingRequest> page = lendingRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/lending-requests");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
