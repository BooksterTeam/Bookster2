package io.bookster.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.bookster.domain.Copy;
import io.bookster.service.CopyService;
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
 * REST controller for managing Copy.
 */
@RestController
@RequestMapping("/api")
public class CopyResource {

    private final Logger log = LoggerFactory.getLogger(CopyResource.class);
        
    @Inject
    private CopyService copyService;
    
    /**
     * POST  /copies : Create a new copy.
     *
     * @param copy the copy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new copy, or with status 400 (Bad Request) if the copy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/copies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Copy> createCopy(@Valid @RequestBody Copy copy) throws URISyntaxException {
        log.debug("REST request to save Copy : {}", copy);
        if (copy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("copy", "idexists", "A new copy cannot already have an ID")).body(null);
        }
        Copy result = copyService.save(copy);
        return ResponseEntity.created(new URI("/api/copies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("copy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /copies : Updates an existing copy.
     *
     * @param copy the copy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated copy,
     * or with status 400 (Bad Request) if the copy is not valid,
     * or with status 500 (Internal Server Error) if the copy couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/copies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Copy> updateCopy(@Valid @RequestBody Copy copy) throws URISyntaxException {
        log.debug("REST request to update Copy : {}", copy);
        if (copy.getId() == null) {
            return createCopy(copy);
        }
        Copy result = copyService.save(copy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("copy", copy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /copies : get all the copies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of copies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/copies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Copy>> getAllCopies(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Copies");
        Page<Copy> page = copyService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/copies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /copies/:id : get the "id" copy.
     *
     * @param id the id of the copy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the copy, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/copies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Copy> getCopy(@PathVariable Long id) {
        log.debug("REST request to get Copy : {}", id);
        Copy copy = copyService.findOne(id);
        return Optional.ofNullable(copy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /copies/:id : delete the "id" copy.
     *
     * @param id the id of the copy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/copies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCopy(@PathVariable Long id) {
        log.debug("REST request to delete Copy : {}", id);
        copyService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("copy", id.toString())).build();
    }

    /**
     * SEARCH  /_search/copies?query=:query : search for the copy corresponding
     * to the query.
     *
     * @param query the query of the copy search
     * @return the result of the search
     */
    @RequestMapping(value = "/_search/copies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Copy>> searchCopies(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Copies for query {}", query);
        Page<Copy> page = copyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/copies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
