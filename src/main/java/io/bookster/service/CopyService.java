package io.bookster.service;

import io.bookster.domain.Copy;
import io.bookster.repository.CopyRepository;
import io.bookster.repository.search.CopySearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Copy.
 */
@Service
@Transactional
public class CopyService {

    private final Logger log = LoggerFactory.getLogger(CopyService.class);
    
    @Inject
    private CopyRepository copyRepository;
    
    @Inject
    private CopySearchRepository copySearchRepository;
    
    /**
     * Save a copy.
     * 
     * @param copy the entity to save
     * @return the persisted entity
     */
    public Copy save(Copy copy) {
        log.debug("Request to save Copy : {}", copy);
        Copy result = copyRepository.save(copy);
        copySearchRepository.save(result);
        return result;
    }

    /**
     *  Get all the copies.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Copy> findAll(Pageable pageable) {
        log.debug("Request to get all Copies");
        Page<Copy> result = copyRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one copy by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Copy findOne(Long id) {
        log.debug("Request to get Copy : {}", id);
        Copy copy = copyRepository.findOne(id);
        return copy;
    }

    /**
     *  Delete the  copy by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Copy : {}", id);
        copyRepository.delete(id);
        copySearchRepository.delete(id);
    }

    /**
     * Search for the copy corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Copy> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Copies for query {}", query);
        return copySearchRepository.search(queryStringQuery(query), pageable);
    }
}
